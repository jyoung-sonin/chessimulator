#!/bin/sh

gpg --quiet --batch --yes --decrypt --passphrase=$SONIN_KEYS_PASSPHRASE --output secret.tar.gz secret.tar.gz.gpg && tar -xvzf secret.tar.gz && ls -la .sonin
