package org.a0z.mpd;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Artist extends Item implements Parcelable {
	public static String singleAlbumFormat="%1 Album";
	public static String multipleAlbumsFormat="%1 Albums";

	private final String name;
	private final String sort;
	//private final boolean isVa;
	private final int albumCount;

	public Artist(String name, int albumCount) {
		this.name=name;
		if (null != name && name.toLowerCase(Locale.getDefault()).startsWith("the ")) {
			sort=name.substring(4);
		} else {
			sort=null;
		}
		this.albumCount=albumCount;
	}

    public Artist(String name) {
        this(name, 0);
    }

    public Artist(Artist a) {
        this.name = a.name;
        this.albumCount = a.albumCount;
        this.sort = a.sort;
    }

	protected Artist(Parcel in) {
		this.name=in.readString();
		this.sort=in.readString();
		this.albumCount=in.readInt();
    }

	public String getName() {
		return name;
	}

	public String sort() {
		return null==sort ? name : sort;
	}

	@Override
	public String subText() {
		if (0==albumCount) {
			return null;
		}

		return String.format(1==albumCount ? singleAlbumFormat : multipleAlbumsFormat, albumCount);
	}

	@Override
    public int compareTo(Item o) {
		if (o instanceof Artist) {
			Artist oa=(Artist)o;
			/*
			if (isVa && !oa.isVa) {
				return -1;
			}
			if (!isVa && oa.isVa) {
				return 1;
			}
			*/
			return sort().compareToIgnoreCase(oa.sort());
		}

		return super.compareTo(o);
	}

    @Override
    public boolean equals(Object o) {
    	return (o instanceof Artist) && ((Artist)o).name.equals(name);
    }

    public boolean isSameOnList(Item o) {
        if (null == o) {
            return false;
        }
        return (name.equals(((Artist)o).getName()));
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.sort);
		dest.writeInt(this.albumCount);
	}

	public static final Parcelable.Creator<Artist> CREATOR =
			new Parcelable.Creator<Artist>() {
            public Artist createFromParcel(Parcel in) {
                return new Artist(in);
            }

            public Artist[] newArray(int size) {
                return new Artist[size];
            }
        };
}
