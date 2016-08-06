package com.users.map.repositories.location;

import com.users.map.storage.model.UserLocation;

public interface ILocationRepository {
    void initLocationFromIP();
    UserLocation getLocation();
    void updateLocation(UserLocation newLocation);
}
