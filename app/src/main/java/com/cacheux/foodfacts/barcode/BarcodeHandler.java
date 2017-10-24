package com.cacheux.foodfacts.barcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle barcode input methods
 */
public class BarcodeHandler {
    static final int CODE_SCAN = 467;

    private BarcodeHandler() {
    }

    public static List<BarcodeMethod> getAvailableMethods() {
        List<BarcodeMethod> list = new ArrayList<>();

        list.add(new DialogMethod());
        list.add(new ZXingMethod());

        return list;
    }
}
