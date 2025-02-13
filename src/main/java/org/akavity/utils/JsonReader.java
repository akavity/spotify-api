package org.akavity.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.akavity.annotations.TestData;
import org.testng.annotations.DataProvider;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonReader {
    @DataProvider
    public Object[][] getData(Method method) throws FileNotFoundException {
        String jsonData = JsonParser.parseReader(new FileReader("src/test/resources/test-data/"
                + method.getAnnotation(TestData.class).jsonFile() + ".json")).toString();

        ArrayList<Object> list = null;
        try {
            list = new Gson().fromJson(jsonData, TypeToken.getParameterized(List.class,
                    Class.forName("org.akavity.models." + method.getAnnotation(TestData.class)
                            .model())).getType());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(list).stream()
                .map(data -> new Object[]{data})
                .toArray(Object[][]::new);
    }
}
