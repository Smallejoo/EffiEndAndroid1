package com.example.gamessarchend.supclasses;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DataStruct implements Parcelable {


    private String dateR;
    private String developer;
    private String genre;
    private String platforms;
    private String title;
    private String description;
    private String imageURL;
    private String trailerURL;


    public DataStruct() {
    }

    // Constructor with all fields
    public DataStruct(String dateR, String developer, String genre, String platforms, String title, String description, String imageURL, String trailerURL) {
        this.dateR = dateR;
        this.developer = developer;
        this.genre = genre;
        this.platforms = platforms;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.trailerURL = trailerURL;
    }

    // Getters
    public String getDateR() {
        return dateR;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatforms() {
        return platforms;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    // Setters
    public void setDateR(String dateR) {
        this.dateR = dateR;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }
    protected DataStruct(Parcel in) {
        dateR = in.readString();
        developer = in.readString();
        genre = in.readString();
        platforms = in.readString();
        title = in.readString();
        description = in.readString();
        imageURL = in.readString();
        trailerURL = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(dateR);
        dest.writeString(developer);
        dest.writeString(genre);
        dest.writeString(platforms);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageURL);
        dest.writeString(trailerURL);
    }
    public static final Creator<DataStruct> CREATOR = new Creator<DataStruct>() {
        @Override
        public DataStruct createFromParcel(Parcel in) {
            return new DataStruct(in);
        }

        @Override
        public DataStruct[] newArray(int size) {
            return new DataStruct[size];
        }
    };
}
