package com.views.panels;

import javax.swing.*;
import java.util.List;

/**
 * Created by Ozgen on 5/14/17.
 */
public interface IPnlResults {

    List getBarcodes();

    void setBarcodes(List barcodes);

    JPanel asPanel();

    List getData();
}
