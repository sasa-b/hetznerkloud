@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Price.Amount
import tech.sco.hetznerkloud.model.LoadBalancerType.Id as LoadBalancerTypeId
import tech.sco.hetznerkloud.model.ServerType.Id as ServerTypeId

@Serializable
data class Pricing(
    val currency: String,
    @JsonNames("floating_ip")
    val floatingIp: FloatingIp,
    @JsonNames("floating_ips")
    val floatingIps: List<FloatingIpPrices>,
    val image: Image,
    @JsonNames("load_balancer_types")
    val loadBalancerTypes: List<LoadBalancerType>,
    @JsonNames("primary_ips")
    val primaryIps: List<PrimaryIpPrices>,
    @JsonNames("server_backup")
    val serverBackup: ServerBackup,
    @JsonNames("server_types")
    val serverTypes: List<ServerType>,
    @JsonNames("vat_rate")
    val vatRate: String,
    val volume: Volume,
) {
    @Serializable
    data class FloatingIp(
        @JsonNames("price_monthly")
        val priceMonthly: Amount,
    )

    @Serializable
    data class FloatingIpPrices(val prices: List<Element>, val type: String) {
        @Serializable
        data class Element(
            val location: String,
            @JsonNames("price_monthly")
            val priceMonthly: Amount,
        )
    }

    @Serializable
    data class Image(
        @JsonNames("price_per_gb_month")
        val pricePerGbMonth: Amount,
    )

    @Serializable
    data class PrimaryIpPrices(val prices: List<Element>, val type: String) {
        @Serializable
        data class Element(
            val location: String,
            @JsonNames("price_hourly")
            val priceHourly: Amount,
            @JsonNames("price_monthly")
            val priceMonthly: Amount,
        )
    }

    @Serializable
    data class ServerBackup(val percentage: String)

    @Serializable
    data class Volume(@JsonNames("price_per_gb_month") val pricePerGbMonth: Amount)

    @Serializable
    data class LoadBalancerType(val id: LoadBalancerTypeId, val name: String, val prices: List<Price>)

    @Serializable
    data class ServerType(val id: ServerTypeId, val name: String, val prices: List<Price>)
}
