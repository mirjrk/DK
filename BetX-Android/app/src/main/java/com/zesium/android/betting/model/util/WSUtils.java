package com.zesium.android.betting.model.util;

import com.google.gson.JsonObject;
import com.zesium.android.betting.model.user.City;
import com.zesium.android.betting.model.user.Question;
import com.zesium.android.betting.model.user.Region;
import com.zesium.android.betting.utils.AppLogger;

/**
 * Created by Ivan Panic_2 on 7/1/2016.
 */
public class WSUtils {

    private static final String TAG = WSUtils.class.getSimpleName();

    public static Region parseRegionData(JsonObject regionData) {
        Region region = new Region();
        try {
            if (regionData.has("Id") && !regionData.get("Id").isJsonNull()) {
                region.setId(regionData.get("Id").getAsInt());
            }
            if (regionData.has("Name") && !regionData.get("Name").isJsonNull()) {
                region.setName(regionData.get("Name").getAsString());
            }
            if (regionData.has("CountryId") && !regionData.get("CountryId").isJsonNull()) {
                region.setCountryId(regionData.get("CountryId").getAsInt());
            }
            if (regionData.has("TimeStamp") && !regionData.get("TimeStamp").isJsonNull()) {
                region.setTimeStamp(regionData.get("TimeStamp").getAsInt());
            }
        } catch (Exception e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
            return null;
        }
        return region;
    }

    public static City parseCityData(JsonObject cityData) {
        City city = new City();
        try {
            if (cityData.has("Id") && !cityData.get("Id").isJsonNull()) {
                city.setId(cityData.get("Id").getAsInt());
            }
            if (cityData.has("Name") && !cityData.get("Name").isJsonNull()) {
                city.setName(cityData.get("Name").getAsString());
            }
            if (cityData.has("CountryCode") && !cityData.get("CountryCode").isJsonNull()) {
                city.setCountryCode(cityData.get("CountryCode").getAsString());
            }
            if (cityData.has("CityAndZipCode") && !cityData.get("CityAndZipCode").isJsonNull()) {
                city.setCityAndZipCode(cityData.get("CityAndZipCode").getAsString());
            }
            if (cityData.has("RegionId") && !cityData.get("RegionId").isJsonNull()) {
                city.setRegionId(cityData.get("RegionId").getAsInt());
            }
            if (cityData.has("TimeStamp") && !cityData.get("TimeStamp").isJsonNull()) {
                city.setTimeStamp(cityData.get("TimeStamp").getAsInt());
            }
        } catch (Exception e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
            return null;
        }
        return city;
    }

    public static Question parseQuestionData(JsonObject questionData) {
        Question question = new Question();
        try {
            if (questionData.has("Id") && !questionData.get("Id").isJsonNull()) {
                question.setId(questionData.get("Id").getAsInt());
            }
            if (questionData.has("Question") && !questionData.get("Question").isJsonNull()) {
                question.setQuestion(questionData.get("Question").getAsString());
            }
            if (questionData.has("LanguageId") && !questionData.get("LanguageId").isJsonNull()) {
                question.setLanguageId(questionData.get("LanguageId").getAsString());
            }
            if (questionData.has("TimeStamp") && !questionData.get("TimeStamp").isJsonNull()) {
                question.setTimeStamp(questionData.get("TimeStamp").getAsInt());
            }
        } catch (Exception e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
            return null;
        }
        return question;
    }
}
