#!/bin/bash

RED='\033[0;31m'
GREEN='\033[0;32m'
RESET='\033[0m' # No Color

###
# params
# $1 *.yml file
# $2 prefix e.g. config_
parse_yaml() {
    local prefix=$2
    local s
    local w
    local fs
    s='[[:space:]]*'
    w='[a-zA-Z0-9_]*'
    fs="$(echo @|tr @ '\034')"
    sed -ne "s|^\($s\)\($w\)$s:$s\"\(.*\)\"$s\$|\1$fs\2$fs\3|p" \
        -e "s|^\($s\)\($w\)$s[:-]$s\(.*\)$s\$|\1$fs\2$fs\3|p" "$1" |
    awk -F"$fs" '{
    indent = length($1)/2;
    vname[indent] = $2;
    for (i in vname) {if (i > indent) {delete vname[i]}}
        if (length($3) > 0) {
            vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
            printf("%s%s%s=(\"%s\")\n", "'"$prefix"'",vn, $2, $3);
        }
    }' | sed 's/_=/+=/g'
}

log () {
    echo -en ${GREEN}
    echo "[${docker.image.name}] $1"
    echo -en ${RESET}
}

error() {
    echo -en ${RED}
    echo "[${docker.image.name}] $1"
    echo -en ${RESET}
}

validate() {
    if [ -z ${CONSUL_HOST_AND_PORT} ]; then
        error "CONSUL_HOST_AND_PORT is required"
        exit 1
    fi
    if [ -z ${CONFIG_PATH} ]; then
        error "CONFIG_PATH is required"
        exit 1
    fi
    if [ -z ${CONSUL_TOKEN} ]; then
        error "CONSUL_TOKEN is required"
        exit 1
    fi
    if [ -z ${SERVICE_NAME} ]; then
        error "SERVICE_NAME is required"
        exit 1
    fi
}

shutdown() {
    deregister_healthcheck ${SERVICE_ID}
    sleep 2
}

# skip consul if run locally
if [ ${LOCAL} ]; then
    log "Starting LOCAL..."
else
#    validate

    log "Starting ..."

#    declare -a CONFIG_RESOURCES=(${config.files})
#    for config_file in "${CONFIG_RESOURCES[@]}"
#    do
#        log "downloading config file from consul: ${config_file}"
#        download_config_from_consul.sh "${CONSUL_HOST_AND_PORT}" "${CONSUL_TOKEN}" \
#        "${CONFIG_PATH}/${config_file}" \
#        "/etc/searchmetrics/${debian.package.name}/${config_file}"

#        if [ "${config_file: -7}" == ".base64" ]; then
            # cut the trailing '.base64' from config_file
#            binary_config_file=${config_file%.base64}

#            log "Converting binary file ${config_file} to ${binary_config_file}"
#            base64 -d /etc/searchmetrics/${debian.package.name}/${config_file} > /etc/searchmetrics/${debian.package.name}/${binary_config_file}
#            rm /etc/searchmetrics/${debian.package.name}/${config_file}
#        fi
#    done

    eval $(parse_yaml /etc/searchmetrics/${debian.package.name}/application.yaml "config_")

    SERVICE_PORT=$config_server_applicationConnectors__port

#    register_healthcheck ${SERVICE_ID} ${SERVICE_NAME} ${SERVICE_PORT}
    sleep 2
fi

trap 'shutdown' SIGTERM
exec service ${debian.package.name} console & wait ${!}

