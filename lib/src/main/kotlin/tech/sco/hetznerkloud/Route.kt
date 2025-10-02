package tech.sco.hetznerkloud

import io.ktor.http.HttpMethod

internal typealias HttpMethodAndPath = Pair<HttpMethod, Path>

internal data class Path(private val rawValue: String) {
    companion object {
        const val BASE = "/v1"
    }

    val value: String
        get() = "$BASE/${rawValue.trimStart('/')}"

    override fun toString(): String = value

    fun withParams(params: RouteParams): Path {
        var path: String = rawValue
        params.forEach { (k, v) -> path = path.replace("{$k}", v) }
        check(!path.contains("{") && !path.contains("}")) {
            "Failed to replace all parameters in path: $path"
        }
        return Path(path)
    }

    fun toRegex(): Regex = value.replace("(\\{\\w+})".toRegex(), "\\\\d+").toRegex()
    fun toSegments(): List<String> = value.split("/").filter { it.isNotEmpty() }
}

internal enum class Route(val value: HttpMethodAndPath) {
    GET_ALL_ACTIONS(Pair(HttpMethod.Get, Path("/actions"))),
    GET_ACTION(Pair(HttpMethod.Get, Path("/actions/{id}"))),

    GET_ALL_SERVERS(Pair(HttpMethod.Get, Path("/servers"))),
    GET_SERVER(Pair(HttpMethod.Get, Path("/servers/{id}"))),
    GET_SERVER_METRICS(Pair(HttpMethod.Get, Path("/servers/{id}/metrics"))),
    GET_SERVER_ACTIONS(Pair(HttpMethod.Get, Path("/servers/{id}/actions"))),
    GET_SERVER_ACTION(Pair(HttpMethod.Get, Path("/servers/actions/{id}"))),
    GET_SERVER_ACTION_FOR_SERVER(Pair(HttpMethod.Get, Path("/servers/{id}/actions/{action_id}"))),
    GET_ALL_SERVER_ACTIONS(Pair(HttpMethod.Get, Path("/servers/actions"))),
    CREATE_SERVER(Pair(HttpMethod.Post, Path("/servers"))),
    UPDATE_SERVER(Pair(HttpMethod.Patch, Path("/servers/{id}"))),
    DELETE_SERVER(Pair(HttpMethod.Delete, Path("/servers/{id}"))),
    ADD_SERVER_TO_PLACEMENT_GROUP(Pair(HttpMethod.Post, Path("/servers/{id}/actions/add_to_placement_group"))),
    ATTACH_ISO_TO_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/attach_iso"))),
    DETACH_ISO_FROM_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/detach_iso"))),
    ATTACH_SERVER_TO_NETWORK(Pair(HttpMethod.Post, Path("/servers/{id}/actions/attach_to_network"))),
    DETACH_SERVER_FROM_NETWORK(Pair(HttpMethod.Post, Path("/servers/{id}/actions/detach_from_network"))),
    SHUTDOWN_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/shutdown"))),
    RESET_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/reset"))),
    SOFT_REBOOT_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/reboot"))),
    POWER_ON_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/poweron"))),
    POWER_OFF_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/poweroff"))),
    RESET_SERVER_ROOT_PASSWORD(Pair(HttpMethod.Post, Path("/servers/{id}/actions/reset_password"))),
    REQUEST_CONSOLE_FOR_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/request_console"))),
    REMOVE_SERVER_FROM_PLACEMENT_GROUP(Pair(HttpMethod.Post, Path("/servers/{id}/actions/remove_from_placement_group"))),
    REBUILD_SERVER_FROM_IMAGE(Pair(HttpMethod.Post, Path("/servers/{id}/actions/rebuild"))),
    ENABLE_SERVER_BACKUP(Pair(HttpMethod.Post, Path("/servers/{id}/actions/enable_backup"))),
    DISABLE_SERVER_BACKUP(Pair(HttpMethod.Post, Path("/servers/{id}/actions/disable_backup"))),
    ENABLE_SERVER_RESCUE_MODE(Pair(HttpMethod.Post, Path("/servers/{id}/actions/enable_rescue"))),
    DISABLE_SERVER_RESCUE_MODE(Pair(HttpMethod.Post, Path("/servers/{id}/actions/disable_rescue"))),
    CREATE_IMAGE_FROM_SERVER(Pair(HttpMethod.Post, Path("/servers/{id}/actions/create_image"))),
    CHANGE_SERVER_TYPE(Pair(HttpMethod.Post, Path("/servers/{id}/actions/change_type"))),
    CHANGE_SERVER_PROTECTION(Pair(HttpMethod.Post, Path("/servers/{id}/actions/change_protection"))),
    CHANGE_SERVER_REVERSE_DNS(Pair(HttpMethod.Post, Path("/servers/{id}/actions/change_dns_ptr"))),
    CHANGE_SERVER_ALIAS_IP_OF_NETWORK(Pair(HttpMethod.Post, Path("/servers/{id}/actions/change_alias_ips"))),

    GET_ALL_DATACENTERS(Pair(HttpMethod.Get, Path("/datacenters"))),
    GET_DATACENTER(Pair(HttpMethod.Get, Path("/datacenters/{id}"))),

    GET_ALL_IMAGES(Pair(HttpMethod.Get, Path("/images"))),
    GET_IMAGE(Pair(HttpMethod.Get, Path("/images/{id}"))),
    GET_IMAGE_ACTIONS(Pair(HttpMethod.Get, Path("/images/{id}/actions"))),
    GET_IMAGE_ACTION(Pair(HttpMethod.Get, Path("/images/actions/{id}"))),
    GET_IMAGE_ACTION_FOR_IMAGE(Pair(HttpMethod.Get, Path("/images/{id}/actions/{action_id}"))),
    GET_ALL_IMAGE_ACTIONS(Pair(HttpMethod.Get, Path("/images/actions"))),
    UPDATE_IMAGE(Pair(HttpMethod.Patch, Path("/images/{id}"))),
    DELETE_IMAGE(Pair(HttpMethod.Delete, Path("/images/{id}"))),
    CHANGE_IMAGE_PROTECTION(Pair(HttpMethod.Post, Path("/images/{id}/actions/change_protection"))),

    GET_ALL_ISOS(Pair(HttpMethod.Get, Path("/isos"))),
    GET_ISO(Pair(HttpMethod.Get, Path("/isos/{id}"))),

    GET_ALL_SERVER_TYPES(Pair(HttpMethod.Get, Path("/server_types"))),
    GET_SERVER_TYPE(Pair(HttpMethod.Get, Path("/server_types/{id}"))),

    GET_ALL_PLACEMENT_GROUPS(Pair(HttpMethod.Get, Path("/placement_groups"))),
    GET_PLACEMENT_GROUP(Pair(HttpMethod.Get, Path("/placement_groups/{id}"))),
    CREATE_PLACEMENT_GROUP(Pair(HttpMethod.Post, Path("/placement_groups"))),
    UPDATE_PLACEMENT_GROUP(Pair(HttpMethod.Patch, Path("/placement_groups/{id}"))),
    DELETE_PLACEMENT_GROUP(Pair(HttpMethod.Delete, Path("/placement_groups/{id}"))),

    GET_ALL_NETWORKS(Pair(HttpMethod.Get, Path("/networks"))),
    GET_NETWORK(Pair(HttpMethod.Get, Path("/networks/{id}"))),
    GET_NETWORK_ACTIONS(Pair(HttpMethod.Get, Path("/networks/{id}/actions"))),
    GET_NETWORK_ACTION(Pair(HttpMethod.Get, Path("/networks/actions/{id}"))),
    GET_NETWORK_ACTION_FOR_NETWORK(Pair(HttpMethod.Get, Path("/networks/{id}/actions/{action_id}"))),
    GET_ALL_NETWORK_ACTIONS(Pair(HttpMethod.Get, Path("/networks/actions"))),
    CREATE_NETWORK(Pair(HttpMethod.Post, Path("/networks"))),
    UPDATE_NETWORK(Pair(HttpMethod.Patch, Path("/networks/{id}"))),
    DELETE_NETWORK(Pair(HttpMethod.Delete, Path("/networks/{id}"))),
    ADD_ROUTE_TO_NETWORK(Pair(HttpMethod.Post, Path("/networks/{id}/actions/add_route"))),
    DELETE_ROUTE_FROM_NETWORK(Pair(HttpMethod.Post, Path("/networks/{id}/actions/delete_route"))),
    ADD_SUBNET_TO_NETWORK(Pair(HttpMethod.Post, Path("/networks/{id}/actions/add_subnet"))),
    DELETE_SUBNET_FROM_NETWORK(Pair(HttpMethod.Post, Path("/networks/{id}/actions/delete_subnet"))),
    CHANGE_NETWORK_IP_RANGE(Pair(HttpMethod.Post, Path("/networks/{id}/actions/change_ip_range"))),
    CHANGE_NETWORK_PROTECTION(Pair(HttpMethod.Post, Path("/networks/{id}/actions/change_protection"))),

    GET_ALL_LOAD_BALANCERS(Pair(HttpMethod.Get, Path("/load_balancers"))),
    GET_LOAD_BALANCER(Pair(HttpMethod.Get, Path("/load_balancers/{id}"))),
    GET_LOAD_BALANCER_ACTIONS(Pair(HttpMethod.Get, Path("/load_balancers/{id}/actions"))),
    GET_LOAD_BALANCER_ACTION(Pair(HttpMethod.Get, Path("/load_balancers/actions/{id}"))),
    GET_LOAD_BALANCER_ACTION_FOR_LOAD_BALANCER(Pair(HttpMethod.Get, Path("/load_balancers/{id}/actions/{action_id}"))),
    GET_ALL_LOAD_BALANCER_ACTIONS(Pair(HttpMethod.Get, Path("/load_balancers/actions"))),
    CREATE_LOAD_BALANCER(Pair(HttpMethod.Post, Path("/load_balancers"))),
    UPDATE_LOAD_BALANCER(Pair(HttpMethod.Patch, Path("/load_balancers/{id}"))),
    DELETE_LOAD_BALANCER(Pair(HttpMethod.Delete, Path("/load_balancers/{id}"))),
    LOAD_BALANCER_ADD_SERVICE(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/add_service"))),
    LOAD_BALANCER_UPDATE_SERVICE(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/update_service"))),
    LOAD_BALANCER_DELETE_SERVICE(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/delete_service"))),
    LOAD_BALANCER_ADD_TARGET(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/add_target"))),
    LOAD_BALANCER_REMOVE_TARGET(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/remove_target"))),
    ATTACH_LOAD_BALANCER_TO_NETWORK(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/attach_to_network"))),
    DETACH_LOAD_BALANCER_FROM_NETWORK(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/detach_from_network"))),
    CHANGE_LOAD_BALANCER_ALGORITHM(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/change_algorithm"))),
    CHANGE_LOAD_BALANCER_REVERSE_DNS(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/change_dns_ptr"))),
    CHANGE_LOAD_BALANCER_PROTECTION(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/change_protection"))),
    CHANGE_LOAD_BALANCER_TYPE(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/change_type"))),
    ENABLE_LOAD_BALANCER_PUBLIC_INTERFACE(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/disable_public_interface"))),
    DISABLE_LOAD_BALANCER_PUBLIC_INTERFACE(Pair(HttpMethod.Post, Path("/load_balancers/{id}/actions/enable_public_interface"))),

    GET_ALL_LOAD_BALANCER_TYPES(Pair(HttpMethod.Get, Path("/load_balancer_types"))),
    GET_LOAD_BALANCER_TYPE(Pair(HttpMethod.Get, Path("/load_balancer_types/{id}"))),

    GET_ALL_SSH_KEYS(Pair(HttpMethod.Get, Path("/ssh_keys"))),
    GET_SSH_KEY(Pair(HttpMethod.Get, Path("/ssh_keys/{id}"))),
    CREATE_SSH_KEY(Pair(HttpMethod.Post, Path("/ssh_keys"))),
    UPDATE_SSH_KEY(Pair(HttpMethod.Patch, Path("/ssh_keys/{id}"))),
    DELETE_SSH_KEY(Pair(HttpMethod.Delete, Path("/ssh_keys/{id}"))),

    GET_ALL_VOLUMES(Pair(HttpMethod.Get, Path("/volumes"))),
    GET_VOLUME(Pair(HttpMethod.Get, Path("/volumes/{id}"))),
    GET_VOLUME_ACTIONS(Pair(HttpMethod.Get, Path("/volumes/{id}/actions"))),
    GET_VOLUME_ACTION(Pair(HttpMethod.Get, Path("/volumes/actions/{id}"))),
    GET_VOLUME_ACTION_FOR_VOLUME(Pair(HttpMethod.Get, Path("/volumes/{id}/actions/{action_id}"))),
    GET_ALL_VOLUME_ACTIONS(Pair(HttpMethod.Get, Path("/volumes/actions"))),
    CREATE_VOLUME(Pair(HttpMethod.Post, Path("/volumes"))),
    UPDATE_VOLUME(Pair(HttpMethod.Patch, Path("/volumes/{id}"))),
    DELETE_VOLUME(Pair(HttpMethod.Delete, Path("/volumes/{id}"))),
    CHANGE_VOLUME_PROTECTION(Pair(HttpMethod.Post, Path("/volumes/{id}/actions/change_protection"))),
    ATTACH_VOLUME_TO_SERVER(Pair(HttpMethod.Post, Path("/volumes/{id}/actions/attach"))),
    DETACH_VOLUME_FROM_SERVER(Pair(HttpMethod.Post, Path("/volumes/{id}/actions/detach"))),
    RESIZE_VOLUME(Pair(HttpMethod.Post, Path("/volumes/{id}/actions/resize"))),

    GET_ALL_CERTIFICATES(Pair(HttpMethod.Get, Path("/certificates"))),
    GET_CERTIFICATE(Pair(HttpMethod.Get, Path("/certificates/{id}"))),
    GET_CERTIFICATE_ACTIONS(Pair(HttpMethod.Get, Path("/certificates/{id}/actions"))),
    GET_CERTIFICATE_ACTION(Pair(HttpMethod.Get, Path("/certificates/actions/{id}"))),
    GET_CERTIFICATE_ACTION_FOR_CERTIFICATE(Pair(HttpMethod.Get, Path("/certificates/{id}/actions/{action_id}"))),
    GET_ALL_CERTIFICATE_ACTIONS(Pair(HttpMethod.Get, Path("/certificates/actions"))),
    CREATE_CERTIFICATE(Pair(HttpMethod.Post, Path("/certificates"))),
    UPDATE_CERTIFICATE(Pair(HttpMethod.Patch, Path("/certificates/{id}"))),
    DELETE_CERTIFICATE(Pair(HttpMethod.Delete, Path("/certificates/{id}"))),
    RETRY_CERTIFICATE_ISSUANCE_OR_RENEWAL(Pair(HttpMethod.Post, Path("/certificates/{id}/actions/retry"))),

    GET_ALL_FIREWALLS(Pair(HttpMethod.Get, Path("/firewalls"))),
    GET_FIREWALL(Pair(HttpMethod.Get, Path("/firewalls/{id}"))),
    GET_FIREWALL_ACTIONS(Pair(HttpMethod.Get, Path("/firewalls/{id}/actions"))),
    GET_FIREWALL_ACTION(Pair(HttpMethod.Get, Path("/firewalls/actions/{id}"))),
    GET_FIREWALL_ACTION_FOR_FIREWALL(Pair(HttpMethod.Get, Path("/firewalls/{id}/actions/{action_id}"))),
    GET_ALL_FIREWALL_ACTIONS(Pair(HttpMethod.Get, Path("/firewalls/actions"))),
    CREATE_FIREWALL(Pair(HttpMethod.Post, Path("/firewalls"))),
    UPDATE_FIREWALL(Pair(HttpMethod.Patch, Path("/firewalls/{id}"))),
    DELETE_FIREWALL(Pair(HttpMethod.Delete, Path("/firewalls/{id}"))),
    APPLY_FIREWALL_TO_RESOURCES(Pair(HttpMethod.Post, Path("/firewalls/{id}/actions/apply_to_resources"))),
    REMOVE_FIREWALL_FROM_RESOURCES(Pair(HttpMethod.Post, Path("/firewalls/{id}/actions/remove_from_resources"))),
    SET_FIREWALL_RULES(Pair(HttpMethod.Post, Path("/firewalls/{id}/actions/set_rules"))),

    GET_ALL_PRIMARY_IPS(Pair(HttpMethod.Get, Path("/primary_ips"))),
    GET_PRIMARY_IP(Pair(HttpMethod.Get, Path("/primary_ips/{id}"))),
    GET_PRIMARY_IP_ACTION(Pair(HttpMethod.Get, Path("/primary_ips/actions/{id}"))),
    GET_ALL_PRIMARY_IP_ACTIONS(Pair(HttpMethod.Get, Path("/primary_ips/actions"))),
    CREATE_PRIMARY_IP(Pair(HttpMethod.Post, Path("/primary_ips"))),
    UPDATE_PRIMARY_IP(Pair(HttpMethod.Patch, Path("/primary_ips/{id}"))),
    DELETE_PRIMARY_IP(Pair(HttpMethod.Delete, Path("/primary_ips/{id}"))),
    ASSIGN_PRIMARY_IP_TO_RESOURCE(Pair(HttpMethod.Post, Path("/primary_ips/{id}/actions/assign"))),
    CHANGE_PRIMARY_IP_REVERSE_DNS(Pair(HttpMethod.Post, Path("/primary_ips/{id}/actions/change_dns_ptr"))),
    CHANGE_PRIMARY_IP_PROTECTION(Pair(HttpMethod.Post, Path("/primary_ips/{id}/actions/change_protection"))),
    UNASSIGN_PRIMARY_IP_FROM_RESOURCE(Pair(HttpMethod.Post, Path("/primary_ips/{id}/actions/unassign"))),

    GET_ALL_FLOATING_IPS(Pair(HttpMethod.Get, Path("/floating_ips"))),
    GET_FLOATING_IP(Pair(HttpMethod.Get, Path("/floating_ips/{id}"))),
    GET_FLOATING_IP_ACTIONS(Pair(HttpMethod.Get, Path("/floating_ips/{id}/actions"))),
    GET_FLOATING_IP_ACTION(Pair(HttpMethod.Get, Path("/floating_ips/actions/{id}"))),
    GET_FLOATING_IP_ACTION_FOR_FLOATING_IP(Pair(HttpMethod.Get, Path("/floating_ips/{id}/actions/{action_id}"))),
    GET_ALL_FLOATING_IP_ACTIONS(Pair(HttpMethod.Get, Path("/floating_ips/actions"))),
    CREATE_FLOATING_IP(Pair(HttpMethod.Post, Path("/floating_ips"))),
    UPDATE_FLOATING_IP(Pair(HttpMethod.Patch, Path("/floating_ips/{id}"))),
    DELETE_FLOATING_IP(Pair(HttpMethod.Delete, Path("/floating_ips/{id}"))),
    ASSIGN_FLOATING_IP_TO_SERVER(Pair(HttpMethod.Post, Path("/floating_ips/{id}/actions/assign"))),
    CHANGE_FLOATING_IP_REVERSE_DNS(Pair(HttpMethod.Post, Path("/floating_ips/{id}/actions/change_dns_ptr"))),
    CHANGE_FLOATING_IP_PROTECTION(Pair(HttpMethod.Post, Path("/floating_ips/{id}/actions/change_protection"))),
    UNASSIGN_FLOATING_IP_FROM_SERVER(Pair(HttpMethod.Post, Path("/floating_ips/{id}/actions/unassign"))),

    GET_ALL_PRICES(Pair(HttpMethod.Get, Path("/pricing"))),

    GET_ALL_STORAGE_BOXES(Pair(HttpMethod.Get, Path("/storage_boxes"))),
    GET_STORAGE_BOX(Pair(HttpMethod.Get, Path("/storage_boxes/{id}"))),
    GET_ALL_STORAGE_BOX_ACTIONS(Pair(HttpMethod.Get, Path("/storage_boxes/actions"))),
    GET_STORAGE_BOX_ACTIONS(Pair(HttpMethod.Get, Path("/storage_boxes/{id}/actions"))),
    GET_STORAGE_BOX_ACTION(Pair(HttpMethod.Get, Path("/storage_boxes/actions/{id}"))),
    GET_STORAGE_BOX_ACTION_FOR_STORAGE_BOX(Pair(HttpMethod.Get, Path("/storage_boxes/{id}/actions/{action_id}"))),
    CHANGE_STORAGE_BOX_PROTECTION(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/actions/change_protection"))),
    CHANGE_STORAGE_BOX_TYPE(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/actions/change_type"))),
    RESET_STORAGE_BOX_PASSWORD(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/actions/reset_password"))),
    UPDATE_STORAGE_BOX_ACCESS_SETTINGS(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/actions/update_access_settings"))),

    GET_STORAGE_BOX_SUBACCOUNTS(Pair(HttpMethod.Get, Path("/storage_boxes/{id}/subaccounts"))),
    GET_STORAGE_BOX_SUBACCOUNT(Pair(HttpMethod.Get, Path("/storage_boxes/{id}/subaccounts/{subaccount_id}"))),
    CREATE_STORAGE_BOX_SUBACCOUNT(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/subaccounts"))),
    UPDATE_STORAGE_BOX_SUBACCOUNT(Pair(HttpMethod.Put, Path("/storage_boxes/{id}/subaccounts/{subaccount_id}"))),
    DELETE_STORAGE_BOX_SUBACCOUNT(Pair(HttpMethod.Delete, Path("/storage_boxes/{id}/subaccounts/{subaccount_id}"))),
    RESET_STORAGE_BOX_SUBACCOUNT_PASSWORD(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/subaccounts/{subaccount_id}/actions/reset_subaccount_password"))),
    UPDATE_STORAGE_BOX_SUBACCOUNT_ACCESS_SETTINGS(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/subaccounts/{subaccount_id}/actions/update_access_settings"))),

    GET_STORAGE_BOX_SNAPSHOTS(Pair(HttpMethod.Get, Path("/storage_boxes/{id}/snapshots"))),
    GET_STORAGE_BOX_SNAPSHOT(Pair(HttpMethod.Get, Path("/storage_boxes/{id}/snapshots/{snapshot_id}"))),
    CREATE_STORAGE_BOX_SNAPSHOT(Pair(HttpMethod.Post, Path("/storage_boxes/{id}/snapshots"))),
    UPDATE_STORAGE_BOX_SNAPSHOT(Pair(HttpMethod.Put, Path("/storage_boxes/{id}/snapshots/{snapshot_id}"))),
    DELETE_STORAGE_BOX_SNAPSHOT(Pair(HttpMethod.Delete, Path("/storage_boxes/{id}/snapshots/{snapshot_id}"))),

    GET_ALL_STORAGE_BOX_TYPES(Pair(HttpMethod.Get, Path("/storage_box_types"))),
    GET_STORAGE_BOX_TYPE(Pair(HttpMethod.Get, Path("/storage_box_types/{id}"))),
    CREATE_STORAGE_BOX(Pair(HttpMethod.Post, Path("/storage_boxes"))),
    UPDATE_STORAGE_BOX(Pair(HttpMethod.Put, Path("/storage_boxes/{id}"))),
    DELETE_STORAGE_BOX(Pair(HttpMethod.Delete, Path("/storage_boxes/{id}"))),
    GET_STORAGE_BOX_CONTENT(Pair(HttpMethod.Get, Path("/storage_boxes/{id}/folders"))),
    ;

    val method: HttpMethod
        get() = value.first

    val path: Path
        get() = value.second
}
