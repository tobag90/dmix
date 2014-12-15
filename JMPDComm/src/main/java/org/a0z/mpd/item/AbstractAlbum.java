/*
 * Copyright (C) 2004 Felipe Gustavo de Almeida
 * Copyright (C) 2010-2014 The MPDroid Project
 *
 * All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice,this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.a0z.mpd.item;

import org.a0z.mpd.Tools;

import java.util.Arrays;
import java.util.Comparator;

/** This class is the generic base for the Album items, abstracted for backend. */
public abstract class AbstractAlbum extends Item {

    protected static final String TAG = "Album";

    private final Artist mArtist;

    private final long mDate;

    /**
     * This {@code Comparator} adds support for sorting by date to the default {@code comparator}.
     */
    public static final Comparator<AbstractAlbum> SORT_BY_DATE = new Comparator<AbstractAlbum>() {
        /**
         * Compares the two specified objects to determine their relative ordering. The ordering
         * implied by the return value of this method for all possible pairs of
         * {@code (lhs, rhs)} should form an <i>equivalence relation</i>.
         * This means that
         * <ul>
         * <li>{@code compare(a, a)} returns zero for all {@code a}</li>
         * <li>the sign of {@code compare(a, b)} must be the opposite of the sign of {@code
         * compare(b, a)} for all pairs of (a,b)</li>
         * <li>From {@code compare(a, b) > 0} and {@code compare(b, c) > 0} it must
         * follow {@code compare(a, c) > 0} for all possible combinations of {@code
         * (a, b, c)}</li>
         * </ul>
         *
         * @param lhs an {@code Object}.
         * @param rhs a second {@code Object} to compare with {@code lhs}.
         * @return an integer < 0 if {@code lhs} is less than {@code rhs}, 0 if they are
         * equal, and > 0 if {@code lhs} is greater than {@code rhs}.
         * @throws ClassCastException if objects are not of the correct type.
         */
        @Override
        public int compare(final AbstractAlbum lhs, final AbstractAlbum rhs) {
            int compare = 0;
            final int leftDate = formattedDate(lhs.mDate);
            final int rightDate = formattedDate(rhs.mDate);

            if (leftDate < rightDate) {
                compare = -1;
            } else if (leftDate > rightDate) {
                compare = 1;
            }

            if (compare == 0) {
                compare = lhs.compareTo(rhs);
            }

            return compare;
        }

        /**
         * This formats the input date to have a minimum of 8 digits to improve comparison.
         *
         * @param date The input date.
         * @return The input date formatted to exactly 8 digits,
         * unless date is less than or equal to 0L.
         */
        private int formattedDate(final long date) {
            final int result;

            if (date > 0L) {
                final StringBuilder stringBuilder = new StringBuilder(8);
                stringBuilder.append(date);

                final int dateLength = stringBuilder.length();

                if (dateLength > 8) {
                    stringBuilder.setLength(8);
                } else if (dateLength < 8) {
                    while (stringBuilder.length() < 8) {
                        stringBuilder.append('0');
                    }
                }
                result = Integer.parseInt(stringBuilder.toString());
            } else {
                result = 0;
            }

            return result;
        }
    };

    private final long mDuration;

    private final boolean mHasAlbumArtist;

    private final String mName;

    private final String mPath;

    private final long mSongCount;

    protected AbstractAlbum(final String name, final Artist artist, final boolean hasAlbumArtist,
            final long songCount, final long duration, final long date, final String path) {
        super();
        mName = name;
        mSongCount = songCount;
        mDuration = duration;
        mDate = date;
        mArtist = artist;
        mHasAlbumArtist = hasAlbumArtist;
        mPath = path;
    }

    @Override
    public boolean doesNameExist(final Item o) {
        final boolean result;

        if (o instanceof AbstractAlbum) {
            final AbstractAlbum a = (AbstractAlbum) o;
            result = mName.equals(a.mName) && mArtist.doesNameExist(a.mArtist);
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Compares an Artist object with a general contract of
     * comparison that is reflexive, symmetric and transitive.
     *
     * @param o The object to compare this instance with.
     * @return True if the objects are equal with regard to te general contract, false otherwise.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object o) {
        Boolean isEqual = null;

        if (this == o) {
            isEqual = Boolean.TRUE;
        } else if (o == null || getClass() != o.getClass()) {
            isEqual = Boolean.FALSE;
        }

        if (isEqual == null || isEqual.equals(Boolean.TRUE)) {
            final AbstractAlbum album = (AbstractAlbum) o;

            final Object[][] equalsObjects = {
                    {mArtist, album.mArtist},
                    {mName, album.mName},
                    {mPath, album.mPath}
            };

            final long[][] equalsLong = {
                    {mDuration, album.mDuration},
                    {mSongCount, album.mSongCount},
                    {mDate, album.mDate}
            };

            if (Tools.isNotEqual(equalsObjects) || Tools.isNotEqual(equalsLong) ||
                    mHasAlbumArtist != album.mHasAlbumArtist) {
                isEqual = Boolean.FALSE;
            }
        }

        if (isEqual == null) {
            isEqual = Boolean.TRUE;
        }

        return isEqual.booleanValue();
    }

    public Artist getArtist() {
        return mArtist;
    }

    public long getDate() {
        return mDate;
    }

    public long getDuration() {
        return mDuration;
    }

    @Override
    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    public long getSongCount() {
        return mSongCount;
    }

    public boolean hasAlbumArtist() {
        return mHasAlbumArtist;
    }

    /**
     * Returns an integer hash code for this Artist. By contract, any two objects for which
     * {@link #equals} returns {@code true} must return the same hash code value. This means that
     * subclasses of {@code Object} usually override both methods or neither method.
     *
     * @return This Artist hash code.
     * @see Object#equals(Object)
     */
    @Override
    public int hashCode() {
        final Object[] objects = {mArtist, mName, mPath};
        int result = Arrays.hashCode(objects);

        if (mHasAlbumArtist) {
            result += 31;
        }

        result = 31 * result + (int) (mDuration ^ (mDuration >>> 32));
        result = 31 * result + (int) (mSongCount ^ (mSongCount >>> 32));
        result = 31 * result + (int) (mDate ^ (mDate >>> 32));

        return result;
    }
}
