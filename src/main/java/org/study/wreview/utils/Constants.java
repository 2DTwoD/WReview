package org.study.wreview.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    @Value("${page.config.rowsInPage}")
    public int rowsInPage;
}
