package br.com.customer.web.exception.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeMessage implements Serializable {
  private static final long serialVersionUID = 1L;

  private String attribute;
  private List<String> errors = new ArrayList<>();

  public AttributeMessage(String attribute, String error) {
    this.attribute = attribute;
    addError(error);
  }

  public void addError(String error) {
    this.errors.add(error);
  }
}
