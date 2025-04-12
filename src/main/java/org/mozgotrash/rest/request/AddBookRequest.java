package org.mozgotrash.rest.request;

import lombok.Data;

@Data
public class AddBookRequest {

    String author;
    String title;
    String imageBase64;
    Integer pageCount;

}
