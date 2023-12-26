package br.com.fipe.FipeTable.service;

import java.util.List;

public interface IDataConversion {
    <T> T dataObtain(String json, Class<T> classe);

    <T> List<T> listObtain(String json, Class<T> classe);
}
