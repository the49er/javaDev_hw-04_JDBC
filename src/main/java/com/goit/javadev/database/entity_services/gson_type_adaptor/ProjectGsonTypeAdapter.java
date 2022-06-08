package com.goit.javadev.database.entity_services.gson_type_adaptor;

import com.goit.javadev.database.entity.project.Project;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public class ProjectGsonTypeAdapter extends TypeAdapter<Project> {
    @Override
    public void write(JsonWriter jsonWriter, Project project) {
        try {
            jsonWriter.beginObject();
            jsonWriter.name("id");
            jsonWriter.value(project.getId());
            jsonWriter.name("name");
            jsonWriter.value(project.getName());
            jsonWriter.name("description");
            jsonWriter.value(project.getDescription());
            jsonWriter.name("date");
            jsonWriter.value(String.valueOf(project.getDate()));
            jsonWriter.name("customerId");
            jsonWriter.value(project.getCustomerId());
            jsonWriter.name("companyId");
            jsonWriter.value(project.getCompanyId());
            jsonWriter.endObject();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Project read(JsonReader jsonReader){
        Project project = new Project();
        String fieldName = null;
        try {
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                JsonToken token = jsonReader.peek();

                if (token.equals(JsonToken.NAME)) {
                    fieldName = jsonReader.nextName();
                }

                if ("id".equals(fieldName)) {
                    project.setId(jsonReader.nextLong());
                }

                if ("name".equals(fieldName)) {
                    project.setName(jsonReader.nextString());
                }

                if ("description".equals(fieldName)) {
                    project.setDescription(jsonReader.nextString());
                }

                if ("date".equals(fieldName)) {
                    project.setDate(LocalDate.parse(jsonReader.nextString()));
                }

                if ("customerId".equals(fieldName)) {
                    project.setCustomerId(jsonReader.nextInt());
                }
                if ("companyId".equals(fieldName)) {
                    project.setCompanyId(jsonReader.nextInt());
                }
            }
            jsonReader.endObject();
            return project;
        }catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
