package com.example.dell.mavride;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Hemanth on 3/13/2015.
 */
public class DriverAllocation {
    public void allocation() {
        String status = "Online";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DriverDetail");
        query.whereEqualTo("DriverStatus", status);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    final int[] ridersCount = new int[1];
                    for (int i = 0; i < userList.size(); i++) {
                        final ParseObject obj = userList.get(i);
                        ridersCount[0] = 0;
                        final String driverId = obj.getString("DriverId");
                        ParseQuery<ParseObject> NoOfRiders = ParseQuery.getQuery("RideRequest");
                        NoOfRiders.whereEqualTo("DriverId", driverId);
                        NoOfRiders.whereEqualTo("Status", "Pending");
                        //must include remianing status
                        NoOfRiders.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> ridesList, ParseException e) {
                                if (e == null) {
                                     ridersCount[0] = 0;
                                    for (int i = 0; i < ridesList.size(); i++) {
                                        ParseObject rideObj = ridesList.get(i);
                                        if (ridersCount[0] >= 3) {
                                            break;
                                        }
                                        ridersCount[0] = ridersCount[0] + rideObj.getInt("NoRiders");
                                    }
                                    if (ridersCount[0] < 3) {
                                        ParseQuery<ParseObject> unallocated = ParseQuery.getQuery("RideRequest");
                                        unallocated.whereEqualTo("Status", "Unallocated");
                                        //Allocation of requests;
                                        unallocated.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> unAllocate, ParseException e) {
                                                if (e == null) {
                                                    if(unAllocate.size() > 0) {
                                                        for (int i = 0; i < unAllocate.size(); i++) {
                                                            ParseObject unAllocateRidersCount = unAllocate.get(i);
                                                            int reqRiders = unAllocateRidersCount.getInt("NoRiders");
                                                            int finalRiders = ridersCount[0] + reqRiders;
                                                            if (finalRiders <= 3) {
                                                                unAllocateRidersCount.put("DriverId", driverId);
                                                                unAllocateRidersCount.put("Status","Pending");
                                                                unAllocateRidersCount.saveInBackground();
                                                                ridersCount[0] = finalRiders;
                                                            }
                                                            if (ridersCount[0] >= 3) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        System.out.print("NO requests");
                                                    }
                                                } else {
                                                    System.out.print("No Pending Requests");
                                                }
                                            }
                                        });
                                    } else {
                                        System.out.print("Count is more than 3");
                                        //ridersCount[0] = 0;
                                    }

                                } else {
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
                                                        unAllocateRidersCount.put("DriverId", driverId);
                                                        unAllocateRidersCount.saveInBackground();
                                                        ridersCount[0] = finalRiders;
                                                    }
                                                    if (ridersCount[0] >= 3) {
                                                        break;
                                                    }
                                                }
                                            } else {
                                                System.out.print("No Pending Requests");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
                else {
                    System.out.print("No Drivers are active");
                }
            }
        });
    }
}