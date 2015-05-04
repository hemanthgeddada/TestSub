package com.example.dell.mavride;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hemanth on 5/3/2015.
 */
public class DriverAllocationPriority {

    public void allocation() {
        final String status = "Online";  // initialising status variable for comparisons
        ParseQuery<ParseObject> priorityRequests = ParseQuery.getQuery("RideRequest");
        priorityRequests.whereEqualTo("Priority", 1); // Checking the list of drivers who are online
        priorityRequests.whereEqualTo("Status", "Unallocated");
        priorityRequests.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> reqList, ParseException e) {
                if (e == null) {
                    final int[] ridersCount = new int[1];
                    for (int i = 0; i < reqList.size(); i++) {
                        final ParseObject reqobj = reqList.get(i);
                        final String[] reqArea = new String[1];
                        String reqLoc = reqobj.getString("Destination");
                        ParseQuery<ParseObject> loc = ParseQuery.getQuery("Location");
                        loc.whereEqualTo("LocName", reqLoc);
                        loc.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                reqArea[0] = object.getString("CampusType");
                            }
                        });
                        ParseQuery<ParseObject> driverStatus = ParseQuery.getQuery("DriverDetail");
                        driverStatus.whereEqualTo("DriverStatus", status); // Checking the list of drivers who are online
                        driverStatus.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> userList, ParseException e) {
                                if (e == null) {
                                    final int[] ridersCount = new int[1];
                                    for (int i = 0; i < userList.size(); i++) {
                                        final String[] cLocationArea = new String[1];
                                        final ParseObject dvrobj = userList.get(i);
                                        final String driverId = dvrobj.getString("DriverId");
                                        final String cLocation = dvrobj.getString("CurrentLocation");
                                        ParseQuery<ParseObject> loc = ParseQuery.getQuery("Location");
                                        loc.whereEqualTo("LocName", cLocation);
                                        loc.getFirstInBackground(new GetCallback<ParseObject>() {
                                            public void done(ParseObject object, ParseException e) {
                                                cLocationArea[0] = object.getString("CampusType");
                                            }
                                        });
                                        if (cLocationArea[0].equals(reqArea[0])) {
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
                                                            ridersCount[0] = ridersCount[0] + reqobj.getInt("NoRiders");
                                                        }
                                                        // if count less than 3  then retrieve the unallocated requests
                                                        if (ridersCount[0] < 3) {
                                                            reqobj.put("DriverId", driverId);
                                                            reqobj.put("Status", "Pending");
                                                            reqobj.saveInBackground();
                                                        }
                                                    } else {
                                                        // all Riders are busy
                                                    }
                                                }
                                            });
                                        } else {
                                            //not in same Campus side
                                        }

                                    }
                                } else {
                                    System.out.print("No drivers");
                                }
                            }
                        });
                    }
                } else {
                    System.out.println("No priority requests");
                    //ridersCount[0] = 0;
                }
            }
        });
    }
}
