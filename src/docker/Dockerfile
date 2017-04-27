# Use phusion/baseimage as base image. To make your builds reproducible, make
# sure you lock down to a specific version, not to `latest`!
# See https://github.com/phusion/baseimage-docker/blob/master/Changelog.md for
# a list of version numbers.
FROM phusion/baseimage:${base.image.version}

RUN apt-get update && apt-get upgrade -y -o Dpkg::Options::="--force-confold" && apt-get install -y wget rsync && \
apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# add repo searchmetrics
RUN wget -O - http://repo.searchmetrics.com/searchmetrics-package-maintainers.key | apt-key add -
RUN echo "deb http://repo.searchmetrics.com/ubuntu lucid main" | tee /etc/apt/sources.list.d/searchmetrics.list

# update repos install java and change timezone
RUN apt-get update && apt-get install -y java-8-oracle && apt-get install -y sm-debian-package-user && \
cp -vf /usr/share/zoneinfo/Europe/Berlin /etc/localtime && echo "Europe/Berlin" | tee /etc/timezone && \
apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Use baseimage-docker's init system.
CMD ["/sbin/my_init"]

#############################################
# ...put your own build instructions here...

# volumes to use as workingdir and for export
#VOLUME ["/basedir", "/workingdir"]

# add and install deb package
ADD ${debian.file.name} /tmp/${debian.file.name}
RUN dpkg -i /tmp/${debian.file.name}

#################################################
# Add scripts
#################################################
RUN mkdir -p /var/log/searchmetrics/${debian.package.name}
RUN chown -R smapp:smapp /var/log/searchmetrics/${debian.package.name}

# add and install run service script for the application
ADD run.sh /etc/service/${debian.package.name}/run
RUN chmod 700 /etc/service/${debian.package.name}/run

###########################################

# Clean up APT when done.
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
