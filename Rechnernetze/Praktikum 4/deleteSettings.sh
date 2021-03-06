#!/bin/bash

sudo /usr/sbin/iptables -F
sudo /usr/sbin/iptables -A INPUT -s 141.22.192.100 -j ACCEPT
sudo /usr/sbin/iptables -A INPUT -s cifs.informatik.haw-hamburg.de -j ACCEPT
sudo /usr/sbin/iptables -A INPUT -s localhost -j ACCEPT

sudo /usr/sbin/iptables -A OUTPUT -d 141.22.192.100 -j ACCEPT
sudo /usr/sbin/iptables -A OUTPUT -d cifs.informatik.haw-hamburg.de -j ACCEPT
sudo /usr/sbin/iptables -A OUTPUT -d localhost -j ACCEPT