DESCRIPTION = "Minimal image with SELinux support (no python)"

IMAGE_FEATURES += "splash ssh-server-openssh"

DISTRO_FEATURES:append = " acl xattr pam selinux"

PREFERRED_PROVIDER_virtual/refpolicy ?= "refpolicy-apacheconf"

LICENSE = "MIT"

IMAGE_INSTALL = "\
	${CORE_IMAGE_BASE_INSTALL} \
	bash \
	util-linux-agetty \
	packagegroup-core-boot \
	packagegroup-selinux-minimal \
	apache2 \
	selinux-python-audit2allow \
	selinux-python-sepolgen-ifgen \
	selinux-python-sepolgen \
	selinux-python-chcat \
	selinux-python-semanage \
	selinux-python-sepolicy \
	procps \
	less \
"

inherit selinux-image
