package com.legacy.blue_skies.data.objects;

import com.google.gson.JsonObject;

public interface IToJson<T extends IToJson<T>> {
   JsonObject toJson();
}
