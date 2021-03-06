#!/bin/sh
# Title: post-install script
# Description: 
#
echo ""
echo "Starting post-install script..."

    echo "Creating the logs directory."
    if [ ! -d /opt/taskqueueprocessor/logs ]; 
    then
	    mkdir /opt/taskqueueprocessor/logs
	fi

    echo "Starting the service..."
    service taskqueueprocessor start
    if [ $? -ne "0" ]; then
        echo "An error has occured starting the process."
    else
        echo "The service has been started."
    fi
    
echo "Ending post-install script"

# exit with a successful exit code
exit 0
