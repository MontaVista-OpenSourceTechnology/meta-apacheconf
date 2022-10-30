FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

require ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'recipes-kernel/linux/linux-yocto_selinux.inc', '', d)}

SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'file://def-selinux.cfg', '', d)}"
