package org.dcsa.edocumentation.transferobjects.validation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CodeCache {
  final String datasetName;
  final String[] columnNames;
  static MultiStringKeySet cache;

  private MultiStringKeySet fetchCache() {
    if (cache == null) {
      cache = PseudoEnumValidator.loadData(datasetName, true, columnNames);
    }
    return cache;
  }

  public boolean isValid(String... keys) {
    return fetchCache().contains(keys);
  }

  static CodeCache of(String datasetName, String... columnNames) {
    return new CodeCache(datasetName, columnNames);
  }
}
