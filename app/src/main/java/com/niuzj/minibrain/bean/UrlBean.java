package com.niuzj.minibrain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;

@Entity
public class UrlBean extends BaseEntityBean implements Parcelable {


    public String type;//用于分组
    public String title;//标题
    public String content;//内容
    public String url;//链接

    public UrlBean() {
    }

    protected UrlBean(Parcel in) {
        id = in.readLong();
        type = in.readString();
        title = in.readString();
        content = in.readString();
        url = in.readString();
        createTime = in.readLong();
        updateTime = in.readLong();
    }

    public static final Creator<UrlBean> CREATOR = new Creator<UrlBean>() {
        @Override
        public UrlBean createFromParcel(Parcel in) {
            return new UrlBean(in);
        }

        @Override
        public UrlBean[] newArray(int size) {
            return new UrlBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeLong(createTime);
        dest.writeLong(updateTime);
    }


    @Override
    public String toString() {
        return "UrlBean{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
