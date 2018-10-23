package com.niuzj.minibrain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;

@Entity
public class TypeBean extends BaseEntityBean implements Parcelable {

    private String type;//类型名称
    private int count;//该类型下，数据量
    private boolean addFlag;//该类型是否是 添加 类型

    public TypeBean() {
    }

    protected TypeBean(Parcel in) {
        type = in.readString();
        count = in.readInt();
        addFlag = in.readByte() != 0;
        createTime = in.readLong();
        updateTime = in.readLong();
    }

    public static final Creator<TypeBean> CREATOR = new Creator<TypeBean>() {
        @Override
        public TypeBean createFromParcel(Parcel in) {
            return new TypeBean(in);
        }

        @Override
        public TypeBean[] newArray(int size) {
            return new TypeBean[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isAddFlag() {
        return addFlag;
    }

    public void setAddFlag(boolean addFlag) {
        this.addFlag = addFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(count);
        dest.writeByte((byte) (addFlag ? 1 : 0));
        dest.writeLong(createTime);
        dest.writeLong(updateTime);
    }


    @Override
    public String toString() {
        return "TypeBean{" +
                "type='" + type + '\'' +
                ", count=" + count +
                ", addFlag=" + addFlag +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
