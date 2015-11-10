/*
 * Copyright (c) 2012 Ball State University
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

package com.udacity.adcs.app.goodintents.content.layout;

import android.net.Uri;
import android.provider.BaseColumns;

import com.udacity.adcs.app.goodintents.content.AppContentProvider;

public interface SearchColumns extends BaseColumns {

    String TABLE_NAME = "search";
    Uri CONTENT_URI = Uri.parse(AppContentProvider.CONTENT_URI + TABLE_NAME);
    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bsu" + TABLE_NAME;
    String CONTENT_ITEMTYPE = "vnd.android.cursor.item/vnd.bsu" + TABLE_NAME;

    // Columns
    String DESC = "search_desc";
    String ITEM_ID = "search_id";
    String NAME = "search_name";
    String ORDER_BY = "search_order_by";
    String TYPE_ID = "search_type_id";

    String CREATE_TABLE =
            "CREATE VIRTUAL TABLE " + TABLE_NAME + " USING fts3 (" +
                    ITEM_ID + ", " +
                    NAME + ", " +
                    DESC + ", " +
                    ORDER_BY + ", " +
                    TYPE_ID + ", " +
                    "tokenize=porter" +
                    ")";

    String DEFAULT_SORT_ORDER = TABLE_NAME + "." + ORDER_BY + "," + TABLE_NAME + "." + TYPE_ID;
}