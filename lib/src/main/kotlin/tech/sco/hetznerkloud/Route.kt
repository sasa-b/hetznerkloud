package tech.sco.hetznerkloud

import io.ktor.http.HttpMethod

internal typealias HttpMethodAndPath = Pair<HttpMethod, Path>

internal data class Path(val value: String) {
    fun withId(id: Long) = Path(value.replace("{id}", id.toString()))
}

internal enum class Route(
    val value: HttpMethodAndPath,
) {
    GET_ALL_ACTIONS(Pair(HttpMethod.Get, Path("/actions"))),
    GET_ACTION(Pair(HttpMethod.Get, Path("/actions/{id}"))),

    GET_ALL_SERVERS(Pair(HttpMethod.Get, Path("/servers"))),
    GET_SERVER(Pair(HttpMethod.Get, Path("/servers/{id}"))),
    GET_SERVER_METRICS(Pair(HttpMethod.Get, Path("/servers/{id}/metrics"))),
    CREATE_SERVER(Pair(HttpMethod.Post, Path("/servers"))),
    UPDATE_SERVER(Pair(HttpMethod.Patch, Path("/servers/{id}"))),
    DELETE_SERVER(Pair(HttpMethod.Delete, Path("/servers/{id}"))),

    GET_ALL_DATACENTERS(Pair(HttpMethod.Get, Path("/datacenters"))),
    GET_DATACENTER(Pair(HttpMethod.Get, Path("/datacenters/{id}"))),

    GET_ALL_IMAGES(Pair(HttpMethod.Get, Path("/images"))),
    GET_IMAGE(Pair(HttpMethod.Get, Path("/images/{id}"))),
    UPDATE_IMAGE(Pair(HttpMethod.Patch, Path("/images/{id}"))),
    DELETE_IMAGE(Pair(HttpMethod.Delete, Path("/images/{id}"))),

    GET_ALL_ISOS(Pair(HttpMethod.Get, Path("/isos"))),
    GET_ISO(Pair(HttpMethod.Get, Path("/isos/{id}"))),

    GET_ALL_SERVER_TYPES(Pair(HttpMethod.Get, Path("/server_types"))),
    GET_SERVER_TYPE(Pair(HttpMethod.Get, Path("/server_types/{id}"))),

    GET_ALL_PLACEMENT_GROUPS(Pair(HttpMethod.Get, Path("/placement_groups"))),
    GET_A_PLACEMENT_GROUP(Pair(HttpMethod.Get, Path("/placement_groups/{id}"))),
    CREATE_PLACEMENT_GROUP(Pair(HttpMethod.Post, Path("/placement_groups"))),
    UPDATE_PLACEMENT_GROUP(Pair(HttpMethod.Patch, Path("/placement_groups/{id}"))),
    DELETE_PLACEMENT_GROUP(Pair(HttpMethod.Delete, Path("/placement_groups/{id}"))),

    GET_ALL_NETWORKS(Pair(HttpMethod.Get, Path("/networks"))),
    GET_NETWORK(Pair(HttpMethod.Get, Path("/networks/{id}"))),
    CREATE_NETWORK(Pair(HttpMethod.Post, Path("/networks"))),
    UPDATE_NETWORK(Pair(HttpMethod.Patch, Path("/networks/{id}"))),
    DELETE_NETWORK(Pair(HttpMethod.Delete, Path("/networks/{id}"))),

    GET_ALL_LOAD_BALANCERS(Pair(HttpMethod.Get, Path("/load_balancers"))),
    GET_LOAD_BALANCER(Pair(HttpMethod.Get, Path("/load_balancers/{id}"))),
    CREATE_LOAD_BALANCER(Pair(HttpMethod.Post, Path("/load_balancers"))),
    UPDATE_LOAD_BALANCER(Pair(HttpMethod.Patch, Path("/load_balancers/{id}"))),
    DELETE_LOAD_BALANCER(Pair(HttpMethod.Delete, Path("/load_balancers/{id}"))),

    GET_ALL_LOAD_BALANCER_TYPES(Pair(HttpMethod.Get, Path("/load_balancer_types"))),
    GET_LOAD_BALANCER_TYPE(Pair(HttpMethod.Get, Path("/load_balancer_types/{id}"))),

    GET_ALL_SSH_KEYS(Pair(HttpMethod.Get, Path("/ssh_keys"))),
    GET_SSH_KEY(Pair(HttpMethod.Get, Path("/ssh_keys/{id}"))),
    CREATE_SSH_KEY(Pair(HttpMethod.Post, Path("/ssh_keys"))),
    UPDATE_SSH_KEY(Pair(HttpMethod.Patch, Path("/ssh_keys/{id}"))),
    DELETE_SSH_KEY(Pair(HttpMethod.Delete, Path("/ssh_keys/{id}"))),

    GET_ALL_VOLUMES(Pair(HttpMethod.Get, Path("/volumes"))),
    GET_VOLUME(Pair(HttpMethod.Get, Path("/volumes/{id}"))),
    CREATE_VOLUME(Pair(HttpMethod.Post, Path("/volumes"))),
    UPDATE_VOLUME(Pair(HttpMethod.Patch, Path("/volumes/{id}"))),
    DELETE_VOLUME(Pair(HttpMethod.Delete, Path("/volumes/{id}"))),

    GET_ALL_CERTIFICATES(Pair(HttpMethod.Get, Path("/certificates"))),
    GET_CERTIFICATE(Pair(HttpMethod.Get, Path("/certificates/{id}"))),
    CREATE_CERTIFICATE(Pair(HttpMethod.Post, Path("/certificates"))),
    UPDATE_CERTIFICATE(Pair(HttpMethod.Patch, Path("/certificates/{id}"))),
    DELETE_CERTIFICATE(Pair(HttpMethod.Delete, Path("/certificates/{id}"))),

    GET_ALL_FIREWALLS(Pair(HttpMethod.Get, Path("/firewalls"))),
    GET_FIREWALL(Pair(HttpMethod.Get, Path("/firewalls/{id}"))),
    CREATE_FIREWALL(Pair(HttpMethod.Post, Path("/firewalls"))),
    UPDATE_FIREWALL(Pair(HttpMethod.Patch, Path("/firewalls/{id}"))),
    DELETE_FIREWALL(Pair(HttpMethod.Delete, Path("/firewalls/{id}"))),
}
