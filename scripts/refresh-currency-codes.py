#!/usr/bin/python3

import csv
import os
import requests
import sys
import xml.etree.ElementTree as ET
from typing import NoReturn


URL = 'https://www.six-group.com/dam/download/financial-information/data-center/iso-currrency/lists/list-one.xml'
TARGET_FILE = os.path.join(
  os.path.dirname(os.path.dirname(__file__)),
  "edocumentation-domain",
  "src/main/resources/validations",
  "currencycodes.csv"
)
COLUMN_NAMES = ['Currency Code', 'Country Name']


def _error(msg) -> "NoReturn":
  print(msg, file=sys.stderr)
  sys.exit(1)


def parse_currency_codes(text):
  try:
    root = ET.fromstring(text)
  except ValueError as e:
    _error("Could not parse the response payload as XML: " + str(e))
  if root.tag != 'ISO_4217':
    _error("Expected the root xml tag to be ISO_4217.  Please verify that the URL is still valid.")
  saw_currency_code = False
  for country_tag in root.iter("CcyNtry"):
    cname_tag = country_tag.find("CtryNm")
    code_tag = country_tag.find("Ccy")
    if cname_tag is None:
      _error("Entry without a country name. Please verify if the format has changed")
    if code_tag is None:
      continue
    code = code_tag.text.strip()
    name = cname_tag.text.strip()
    yield code, name
    saw_currency_code = True

  if not saw_currency_code:
    _error("Could not find a single currency code in the format. Please verify that URL / format is still correct.")


def main():
  response = requests.get(URL)

  with open(TARGET_FILE, "w") as fd:
    csv_writer = csv.DictWriter(fd, fieldnames=COLUMN_NAMES)
    csv_writer.writeheader()
    for fields in parse_currency_codes(response.text):
      assert len(fields) == len(COLUMN_NAMES), "COLUMN_NAMES is not aligned with the number of fields parsed"
      row = dict(zip(COLUMN_NAMES, fields))
      csv_writer.writerow(row)


if __name__ == '__main__':
  main()
