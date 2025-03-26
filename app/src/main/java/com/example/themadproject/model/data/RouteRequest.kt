package com.example.themadproject.model.data

import com.example.myapplication.model.data.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.libraries.places.api.model.EncodedPolyline
import com.google.android.libraries.places.api.model.Leg
import com.google.android.libraries.places.api.model.RoutingParameters.RoutingPreference
import com.google.android.libraries.places.api.model.RoutingParameters.TravelMode



data class RouteRequest(
    val origin: LocationWrapper,
    val destination: LocationWrapper,
    val travelMode: String = "WALK",
    val routingPreference: String? = null,
    val computeAlternativeRoutes: Boolean = false
)
data class LocationWrapper(
    val location: LatLngWrapper
)
data class LatLngWrapper(
    val latLng: LatLngCords
)
data class LatLngCords(
    val latitude: Double,
    val longitude: Double
)
data class RouteResponse(
    val routes: List<Route>
)
data class Route(
    val polyline: OverviewPolyline,
    val legs: List<Leg>?,
    val duration: String? = legs?.firstOrNull()?.duration?.toString()
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
