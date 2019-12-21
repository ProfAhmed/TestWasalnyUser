package com.example.testwasalnyuser.trip_node;

import java.io.Serializable;

public class VehicleInfo implements Serializable {
    private BrandNames brandNames;
    private CategoryNames categoryNames;
    private ColorNames colorNames;
    private RideCategoryNames rideCategoryNames;
    private TypeNames typeNames;
    private String colorCode;

    public BrandNames getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(BrandNames brandNames) {
        this.brandNames = brandNames;
    }

    public CategoryNames getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(CategoryNames categoryNames) {
        this.categoryNames = categoryNames;
    }

    public ColorNames getColorNames() {
        return colorNames;
    }

    public void setColorNames(ColorNames colorNames) {
        this.colorNames = colorNames;
    }

    public RideCategoryNames getRideCategoryNames() {
        return rideCategoryNames;
    }

    public void setRideCategoryNames(RideCategoryNames rideCategoryNames) {
        this.rideCategoryNames = rideCategoryNames;
    }

    public TypeNames getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(TypeNames typeNames) {
        this.typeNames = typeNames;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
