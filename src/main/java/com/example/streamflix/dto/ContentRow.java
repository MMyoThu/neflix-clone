package com.example.streamflix.dto;

import java.util.List;

public record ContentRow(String title, List<CatalogCard> items) {
}
