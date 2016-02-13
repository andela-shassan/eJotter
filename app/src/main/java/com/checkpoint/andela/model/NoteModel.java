package com.checkpoint.andela.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Here is the model that represent a single note.
 * It consists of the note title, note content and the date it was taken.
 * Created by andela on 03/02/2016.
 */
public class NoteModel implements Parcelable {
    public Long _id;
    private String title;
    private String content;
    private String dateTime;
    private String trashed = null;

    public NoteModel(){
        this.dateTime = setDateTime();
        setTrashed("n");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String setDateTime() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public String getDate() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getId() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTrashed() {
        return trashed;
    }

    public void setTrashed(String trashed) {
        this.trashed = trashed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.dateTime);
        dest.writeString(this.trashed);
    }

    protected NoteModel(Parcel in) {
        this._id = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        this.dateTime = in.readString();
        this.trashed = in.readString();
    }

    public static final Creator<NoteModel> CREATOR = new Creator<NoteModel>() {
        public NoteModel createFromParcel(Parcel source) {
            return new NoteModel(source);
        }

        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };
}