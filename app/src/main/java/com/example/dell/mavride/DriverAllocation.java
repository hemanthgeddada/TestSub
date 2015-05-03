package com.example.dell.mavride;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hemanth on 3/13/2015.
 */
public class DriverAllocation {
    public void allocation() {
        String status = "Online";  // initialising status variable for comparisons
        ParseQuery<ParseObject> driverStatus = ParseQuery.getQuery("DriverDetail");
        driverStatus.whereEqualTo("DriverStatus", status); // Checking the list of drivers who are online
        driverStatus.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    final int[] ridersCount = new int[1];
                    for (int i = 0; i < userList.size(); i++) {
                        final ParseObject obj = userList.get(i);
                        ridersCount[0] = 0;
                        final String driverId = obj.getString("DriverId");
                        // Retrieving the allocated request for each individual driver
                        ParseQuery<ParseObject> NoOfRiders_Pending = ParseQuery.getQuery("RideRequest");
                        NoOfRiders_Pending.whereEqualTo("DriverId", driverId);
                        NoOfRiders_Pending.whereEqualTo("Status", "Pending");

                        ParseQuery<ParseObject> NoOfRiders_Pickedup = ParseQuery.getQuery("RideRequest");
                        NoOfRiders_Pickedup.whereEqualTo("DriverId", driverId);
                        NoOfRiders_Pickedup.whereEqualTo("Status", "PickedUp");

                        ParseQuery<ParseObject> NoOfRiders_Waiting = ParseQuery.getQuery("RideRequest");
                        NoOfRiders_Waiting.whereEqualTo("DriverId", driverId);
                        NoOfRiders_Waiting.whereEqualTo("Status", "Waiting");

                        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                        queries.add(NoOfRiders_Pending);
                        queries.add(NoOfRiders_Pickedup);
                        queries.add(NoOfRiders_Waiting);

                        ParseQuery<ParseObject> NoOfRiders = ParseQuery.or(queries);
                        NoOfRiders.orderByAscending("updatedAt");
                        NoOfRiders.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> ridesList, ParseException e) {
                                if (e == null) {
                                    ridersCount[0] = 0;
                                    // Checking the count of allocated riders to a individual driver
                                    for (int i = 0; i < ridesList.size(); i++) {
                                        ParseObject rideObj = ridesList.get(i);
                                        if (ridersCount[0] >= 3) {
                                            break;
                                        }
                                        ridersCount[0] = ridersCount[0] + rideObj.getInt("NoRiders");
                                    }
                                    // if count less than 3  then retrieve the unallocated requests
                                    if (ridersCount[0] < 3) {
                                        ParseQuery<ParseObject> unallocated = ParseQuery.getQuery("RideRequest");
                                        unallocated.whereEqualTo("Status", "Unallocated");
                                        //Allocation of requests;
                                        unallocated.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> unAllocate, ParseException e) {
                                                if (e == null) {
                                                    // Adding the new requests ride count to the allocated requests
                                                    if (unAllocate.size() > 0) {
                                                        for (int i = 0; i < unAllocate.size(); i++) {
                                                            ParseObject unAllocateRidersCount = unAllocate.get(i);
                                                            int reqRiders = unAllocateRidersCount.getInt("NoRiders");
                                                            int finalRiders = ridersCount[0] + reqRiders;
                                                            if (finalRiders <= 3) {
                                                                // Assigned the request to the driver
                                                                unAllocateRidersCount.put("DriverId", driverId);
                                                                unAllocateRidersCount.put("Status", "Pending");
                                                                unAllocateRidersCount.saveInBackground();
                                                                ridersCount[0] = finalRiders;
                                                            }
                                                            if (ridersCount[0] >= 3) {
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        // No pending requests
                                                        System.out.print("No requests");
                                                    }
                                                } else {
                                                    System.out.print("No Pending Requests");
                                                }
                                            }
                                        });
                                    } else {
                                        System.out.println("Count is more than 3");
                                        //ridersCount[0] = 0;
                                    }

                                } else {
                                    // This blocks works only for the first time when there are no riders and have pending requests
                                    ParseQuery<ParseObject> unallocated = ParseQuery.getQuery("RideRequest");
                                    unallocated.whereEqualTo("Status", "Unallocated");
                                    //Allocation of requests;
                                    unallocated.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> unAllocate, ParseException e) {
                                            if (e == null) {
                                                for (int i = 0; i < unAllocate.size(); i++) {
                                                    ParseObject unAllocateRidersCount = unAllocate.get(i);
                                                    int reqRiders = unAllocateRidersCount.getInt("NoRiders");
                                                    int finalRiders = ridersCount[0] + reqRiders;
                                                    if (finalRiders <= 3) {
                                                        // Allocating the driver id to the request
                                                        unAllocateRidersCount.put("DriverId", driverId);
                                                        unAllocateRidersCount.saveInBackground();
                                                        ridersCount[0] = finalRiders;
                                                    }
                                                    if (ridersCount[0] >= 3) {
                                                        break;
                                                    }
                                                }
                                            } else {
                                                System.out.println("No Pending Requests");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                } else {
                    System.out.println("No Drivers are active");
                }
            }
        });
    }
}