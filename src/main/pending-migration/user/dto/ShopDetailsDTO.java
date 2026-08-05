package com.gutfriendly.app.user.dto;
import java.util.List;

public class ShopDetailsDTO {

    private int shopId;
    private String shopName;
    private String category;

    private Double userTrustScore;
    private Double inspectionTrustScore;
    private Double finalGutTrustScore;

    private String locality;
    private String pincode;

    private List<String> imageUrls;

    public ShopDetailsDTO() {
    }

    public ShopDetailsDTO(
            int shopId,
            String shopName,
            String category,
            Double userTrustScore,
            Double inspectionTrustScore,
            Double finalGutTrustScore,
            String locality,
            String pincode,
            List<String> imageUrls) {

        this.shopId = shopId;
        this.shopName = shopName;
        this.category = category;
        this.userTrustScore = userTrustScore;
        this.inspectionTrustScore = inspectionTrustScore;
        this.finalGutTrustScore = finalGutTrustScore;
        this.locality = locality;
        this.pincode = pincode;
        this.imageUrls = imageUrls;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getUserTrustScore() {
        return userTrustScore;
    }

    public void setUserTrustScore(Double userTrustScore) {
        this.userTrustScore = userTrustScore;
    }

    public Double getInspectionTrustScore() {
        return inspectionTrustScore;
    }

    public void setInspectionTrustScore(Double inspectionTrustScore) {
        this.inspectionTrustScore = inspectionTrustScore;
    }

    public Double getFinalGutTrustScore() {
        return finalGutTrustScore;
    }

    public void setFinalGutTrustScore(Double finalGutTrustScore) {
        this.finalGutTrustScore = finalGutTrustScore;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}