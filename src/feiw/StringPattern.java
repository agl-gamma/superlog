/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package feiw;

public final class StringPattern {
    private final String mPattern;
    private final char[] mLowCasePattern;
    private final int[] mTable;
    private final boolean mCaseSensitive;

    @Override
    public final String toString() {
        return mPattern;
    }

    public StringPattern(final String pattern, final boolean caseSensitive) {
        mPattern = pattern;
        mCaseSensitive = caseSensitive;
        if (!caseSensitive) {
            mLowCasePattern = pattern.toLowerCase().toCharArray();
            mTable = createTable(mLowCasePattern);
        } else {
            mLowCasePattern = null;
            mTable = null;
        }
    }

    public int isContainedBy(final String str) {
        if (mCaseSensitive) {
            return str.indexOf(mPattern);
        }

        final int slen = str.length();
        final int plen = mLowCasePattern.length;
        final char[] pat = mLowCasePattern;
        final int[] t = mTable;
        int m = 0, i = 0;
        while (((m + i) < slen)) {
            if (Character.toLowerCase(str.charAt(m + i)) == pat[i]) {
                if (i == plen - 1) {
                    return m;
                }
                i++;
            } else {
                m += i - t[i];
                i = t[i] > -1 ? t[i] : 0;
            }
        }
        return -1;
    }

    static private int[] createTable(final char[] pattern) {
        int patlen = pattern.length;
        int[] t = new int[patlen];
        int i = 2, j = 0;
        t[0] = -1;
        t[1] = 0;
        while (i < patlen) {
            if (pattern[i - 1] == pattern[j]) {
                t[i] = j + 1;
                j++;
                i++;
            } else if (j > 0) {
                j = t[j];
            } else {
                t[i] = 0;
                i++;
                j = 0;
            }
        }
        return t;
    }
}
