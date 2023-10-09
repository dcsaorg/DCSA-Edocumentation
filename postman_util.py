#!/usr/bin/python3

import itertools
import json
import os
import re
import sys


def _error(msg):
  print(msg, file=sys.stderr)
  sys.exit(1)


def _ensure_dir(d):
  os.makedirs(d, exist_ok=True)


def extract(postman_collection, output_dir):
  with open(postman_collection) as fd:
      d = json.load(fd)
  variables_dir = os.path.join(output_dir, "variables")
  variables = d.get("variable")
  if variables:
    _ensure_dir(variables_dir)
    with os.scandir(variables_dir) as dir_iter:
      for p in dir_iter:
        os.unlink(p.path)
    with open(os.path.join(variables_dir, "order.txt", "wt", encoding="utf-8")) as ofd:
      for var in variables:
        key = var["key"]
        value = var["value"]
        with open(os.path.join(variables_dir, key), "wt", encoding="utf-8") as vfd:
          vfd.write(value)
        ofd.write(f"{key}\n")

def reassemble(data_dir, postman_collection):
  with open(postman_collection) as fd:
    d = json.load(fd)

  variables_dir = os.path.join(data_dir, "variables")
  try:
    del d["variable"]
  except KeyError:
    pass
  variables = []
  d["variable"] = variables
  if os.path.isdir(variables_dir):
    order = {}
    try:
      with open(os.path.join(variables_dir, "order.txt"), "rt", encoding="utf-8") as ofd:
        for line in ofd:
          order[line.strip()] = len(order)
    except FileNotFoundError:
      pass
    with os.scandir(variables_dir) as dir_iter:
      for p in sorted(dir_iter, key=lambda p: (order.get(p.name, len(order)), p.name)):
        if p.name == "order.txt":
          continue
        with open(p.path, "rt", encoding="utf-8") as vfd:
          key = p.name
          value = vfd.read()
          variables.append(
            {
              "key": key,
              "value": value,
            }
          )

  to_tabs = re.compile("^(?: {6})+")
  with open(postman_collection + ".new", "w") as wfd:
    serialized = json.dumps(d, indent=6, ensure_ascii=False)
    for line in serialized.splitlines(keepends=True):
      # Postman uses "tab + 2 spaces" per level of indentation with each tab being ~4 spaces.
      # The Python API cannot do that. But it can do 6 spaces, which we can manually massage
      # into tab + spaces as needed, while we cry a bit in our sleep over having to do this.
      m = to_tabs.search(line)
      if m:
        l = len(m.group())
        prefix = "".join(itertools.repeat("\t", l//6))
        if l % 6 != 0:
          prefix += "".join(itertools.repeat(" ", l % 6))
        line = prefix + line[l:]
      wfd.write(line)
  os.rename(postman_collection + ".new", postman_collection)


def main():
  if len(sys.argv) != 4:
    print("Usage: postman_util.py extract postman_collection.json output-dir")
    print("       postman_util.py reassemble input-dir postman_collection.json")
    _error("Bad arguments")
  _, action, arg1, arg2 = sys.argv

  if action == "extract":
    extract(arg1, arg2)
  elif action == "reassemble":
    reassemble(arg1, arg2)
  else:
    _error(f"Unsupported action {action}")


if __name__ == "__main__":
  main()
