#!/bin/bash

PIHOST=pi@doh.local
TEMPDIR=/tmp

ssh $PIHOST rm -rf '$HOME'/app
scp $1 $PIHOST:$TEMPDIR
ssh $PIHOST unzip -d '$HOME' $TEMPDIR/$(basename $1)
ssh $PIHOST "sudo systemctl restart doh.service"
