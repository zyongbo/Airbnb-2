package com.example.toshiba.airbnb.Profile.ViewListingAndYourBooking.EditListing.DTO;

/**
 * Created by TOSHIBA on 30/10/2017.
 */

public class DTOAmenitiesSpace {
    private final boolean kitchen;
    private final boolean laundry;
    private final boolean parking;
    private final boolean elevator;
    private final boolean pool;
    private final boolean gym;

    public DTOAmenitiesSpace(boolean kitchen, boolean laundry, boolean parking, boolean elevator,
                             boolean pool, boolean gym){
        this.kitchen = kitchen;
        this.laundry = laundry;
        this.parking = parking;
        this.elevator = elevator;
        this.pool = pool;
        this.gym = gym;
    }
}
