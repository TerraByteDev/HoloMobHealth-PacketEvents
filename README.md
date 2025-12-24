# HoloMobHealth-PacketEvents (WIP)
An addon plugin for HoloMobHealth to support PacketEvents instead of ProtocolLib.\
This is a very simple plugin that implements all the code seen [here](https://github.com/LOOHP/HoloMobHealth/tree/master/common/src/main/java/com/loohp/holomobhealth/platform).\
Very little modifications to code have been made, apart from making it support PacketEvents.

> [!WARNING]
> This plugin **REQUIRES** v2.3.18.0 of HoloMobHealth, or higher.\
> This plugin will NOT boot otherwise.


## Why use another plugin?
PacketEvents shades Adventure, but doesn't relocate it.\
HoloMobHealth shades Adventure **and** relocates it.

This creates several issues when it comes to using Adventure, which HMH uses heavily.\
It is possible to do this through Reflection within HMH itself, however that impacts maintainability and readability.

In the end, we decided to split PacketEvents support into two parts:
1. Remove the inherent dependency on ProtocolLib.
2. Allow external plugins like this to register their own ProtocolProvider.

This allows us to use the now-reflected Adventure API for HoloMobHealth, and the normal Adventure which PE / the server will provide.

## Setup
1. Install the latest version of PacketEvents, and this plugin.
2. Start your server

## Permissions
Requires the `holomobhealthpacketevents.checkupdate` permission to check for updates on join.
