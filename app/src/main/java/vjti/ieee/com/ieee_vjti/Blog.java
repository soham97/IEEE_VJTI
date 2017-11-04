package vjti.ieee.com.ieee_vjti;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sohamdeshmukh on 04/11/17.
 */

public class Blog implements Parcelable{

    private String mId;
    private String mDate;
    private String mTitle="";
    private String mSubject="";

    public Blog(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mDate);
        dest.writeString(mTitle);
        dest.writeString(mSubject);
    }

    protected Blog(Parcel in) {
        mId = in.readString();
        mDate = in.readString();
        mTitle = in.readString();
        mSubject = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blog blog = (Blog) o;

        if (mId != null ? !mId.equals(blog.mId) : blog.mId != null) return false;
        if (mDate != null ? !mDate.equals(blog.mDate) : blog.mDate != null) return false;
        if (mTitle != null ? !mTitle.equals(blog.mTitle) : blog.mTitle != null) return false;
        return mSubject != null ? mSubject.equals(blog.mSubject) : blog.mSubject == null;
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mSubject != null ? mSubject.hashCode() : 0);
        return result;
    }

    public static final Parcelable.Creator<Blog> CREATOR = new Parcelable.Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel source) {
            return new Blog(source);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
