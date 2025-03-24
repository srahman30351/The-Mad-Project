package com.example.themadproject.model.data


import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.libraries.places.api.model.EncodedPolyline
import com.google.android.libraries.places.api.model.Leg
import com.google.android.libraries.places.api.model.RoutingParameters.RoutingPreference
import com.google.android.libraries.places.api.model.RoutingParameters.TravelMode
import com.squareup.okhttp.Route


data class RouteRequest(
    val origin: Location,
    val destination: Location,
    val travelMode: String = "WALKING",
    val routingPreference: String = "TRAFFIC_AWARE",
    val computeAlernativeRoutes: Boolean = false
)
data class Location(
    val LatLng: LatLng
)
data class RouteResponse(
    val routes: List<Route>
)
data class Route(
    val overviewPolyline: OverviewPolyline,
    val legs: List<Leg>
)
data class OverviewPolyline(
    val encodedPolyline: String
)
data class Leg(
    val duration: Duration,
    val distance: Distance
)
data class Duration(
    val text: String,
    val value: Int
)
data class Distance(
    val text: String,
    val value: Int
)
