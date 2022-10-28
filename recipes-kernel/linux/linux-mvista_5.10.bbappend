require ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '../../../meta-selinux/recipes-kernel/linux/linux-yocto_selinux.inc', '', d)}
