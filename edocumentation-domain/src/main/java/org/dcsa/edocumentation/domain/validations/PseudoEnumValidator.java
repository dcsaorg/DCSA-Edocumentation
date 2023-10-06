package org.dcsa.edocumentation.domain.validations;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PseudoEnumValidator implements ConstraintValidator<PseudoEnum, String> {

  private static final Map<String, Set<String>> CACHE = new ConcurrentHashMap<>();

  private Set<String> allowedValues;

  @Override
  public void initialize(PseudoEnum constraintAnnotation) {
    var name = constraintAnnotation.value();
    var columnName = constraintAnnotation.column();
    allowedValues = fetchData(name, columnName);
  }

  private Set<String> fetchData(String datasetName, String columnName) {
    var cacheKey = datasetName + "!" + columnName;
    Set<String> values = CACHE.get(cacheKey);
    if (values != null) {
      return values;
    }
    values = loadData(datasetName, false, columnName).asSingleKeySet();
    CACHE.put(cacheKey, values);
    return values;
  }

  @SneakyThrows(IOException.class)
  static MultiStringKeySet loadData(String datasetName, boolean ignoreNullValues, String ... columnNames) {
    InputStream in = PseudoEnumValidator.class.getClassLoader().getResourceAsStream("validations/" + datasetName);
    if (in == null) {
      throw new IllegalArgumentException("No data set called validations/" + datasetName);
    }
    if (columnNames.length < 1) {
      columnNames = new String[]{""};
    }
    var mapper = new CsvMapper();
    var objectReader = mapper.readerForListOf(String.class)
      .with(CsvParser.Feature.WRAP_AS_ARRAY)
      .with(CsvParser.Feature.EMPTY_STRING_AS_NULL);
    var values = new MultiStringKeySet(columnNames.length);
    try (var r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
         var contents = objectReader.<List<String>>readValues(r)) {
      ensureHasNext(contents, datasetName);
      var headers = contents.next();
      var columnIndexes = Arrays.stream(columnNames).map(columnName -> {
        if (columnName.isBlank()) {
          return 0;
        }
        var columnIndex = headers.indexOf(columnName);
        if (columnIndex < 0) {
          throw new IllegalArgumentException("Cannot find the column with header \"" + columnName
            + "\" in the dataset validations/" + datasetName);
        }
        return columnIndex;
      }).toList();
      int maxColumnIndex = columnIndexes.stream().max(Integer::compareTo).orElseThrow();

      ensureHasNext(contents, datasetName);
      do {
        var row = contents.next();
        if (maxColumnIndex >= row.size()) {
          throw new IllegalArgumentException("The dataset in validations/" + datasetName + " have rows that did not have at least " + (maxColumnIndex + 1) + " column(s).");
        }
        var keys = columnIndexes.stream().map(row::get).toArray(String[]::new);
        if (Arrays.stream(keys).anyMatch(Objects::isNull)) {
          if (ignoreNullValues) {
            continue;
          }
          throw new IllegalArgumentException("The dataset validations/" + datasetName + " had cells with null values, which are not supported!");
        }
        values.add(keys);
      } while (contents.hasNext());
    }

    return values;
  }

  private static void ensureHasNext(Iterator<?> it, String dataset) {
    if (!it.hasNext()) {
      throw new IllegalArgumentException("The dataset validations/" + dataset + " must have at least two lines");
    }
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (null == value || this.allowedValues.contains(value)) {
      return true;
    }
    constraintValidatorContext.disableDefaultConstraintViolation();
    String values = String.join(", ", this.allowedValues);
    if (this.allowedValues.size() > 20 ) {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
        "The value can not be recognised."
      ).addConstraintViolation();
    }
    else {
      constraintValidatorContext.buildConstraintViolationWithTemplate(
        "The value should have been one of: " + values
      ).addConstraintViolation();
    }
    return false;
  }
}
