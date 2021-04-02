package br.com.elisangelo.todoList.config.validation;

public class FormDto {

    private String field;
    private String error;
    
    public FormDto(String field, String error) {
        this.field = field;
        this.error = error;
    }
    public String getField() {
        return field;
    }
    public String getError() {
        return error;
    }
}
