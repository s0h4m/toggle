[![Build Status](https://travis-ci.org/triveous/Feature-Toggle.svg?branch=master)](https://travis-ci.org/triveous/Feature-Toggle)

##A simple android library to add Feature-Toggle capability in your app 

###Philosophy

A feature toggle, is a technique in software development that helps alter the state of a feature in an application dynamically. For more example, check out [this link.](https://en.wikipedia.org/wiki/Feature_toggle)

###Example
So say, you'd like to disable features like video (or analytics) for a certain group of users (based on api levels and devices) and enable them for the rest, you would need to do the following

####Step 1: Create a configuration file in the following json format and host it somewhere (on your server, S3, etc.) 
```javascript
{"name": "myapp", "features":
	[
	    {"name":"video", "default": "enabled", "rules":[
	    	{"state": "disabled", "value": 
	    		{"apilevel_min": 21, "apilevel_max": 23, "device":[{"manufacturer":"xiaomi","model":"mi3"}, {"manufacturer":"samsung", "model":"s4"}]}
		}, 
	    	{"state": "disabled", "value": {"appversion_max": 13}}
	    ]},
	    {"name":"crash_reporting", "rules":[
	    	   	{"state": "disabled", "value": 
	    	   		{"appversion": 11, "buildtype": "false"}
	    	   	}
	    	]
	    },
	    {"name":"mixpanel","default": "enabled", "rules":[
	    	{"state": "disabled", "value": 
	    		{"device":[{"model":"Google Nexus 5 - 5.1.0 - API 22 - 1080x1920"}, {"manufacturer":"samsung", "model":"s4"}]}
		}
	    ]}
	]
}
```

This configuration file allows you to:
- disable the 'video' feature when all of the following get satisfied: 
     - api level between 21 and 23
     - the device type is either a xiaomi mi3 or a samsung s4
- disable the 'video' feature if the app version is less than 13
- enable the 'video' feature otherwise (default: enabled)
- disable the 'mixpanel' feature if the device is a google nexus 5 or a samsung s4
- enable the 'mixpanel' feature otherwise (default: enabled)

You can find more information on the configuration parameters and how they work in the wiki [here](https://github.com/s0h4m/toggle/wiki). 

####Step 2: Download a new configuration
You can download a new configuration in any of the following ways:

- By passing a url
```java
Toggle.with(context).setConfig(myUrl);
```

- Downloading the config manually and then pass the same to Toggle as a String (in the Config JSON format)
```java
String configInJson = ... // my custom code for downloading the config from my server and retrieving it as a json
Toggle.with(context).setConfig(configInJson);
```
- Downloading the config manually and then pass the same to Toggle as a Config object
```java
Config config = ... // my custom call (say Retrofit for example) for retrieving the Config object from my server
Toggle.with(context).setConfig(config);
```

Once setConfig is called (in any form) the config is then cached locally, so you can always check for a feature later


####Step 3: Check for the state of a feature at any time (even if you are offline)
You can check for a feature using the check method
```java
Toggle.with(context).check("custom_network_component").defaultState(Toggle.ENABLED).start(new cc.soham.toggle.callbacks.Callback() {
            @Override
            public void onStatusChecked(CheckResponse checkResponse) {
                updateUiAfterResponse(checkResponse.featureName, checkResponse.state, checkResponse.featureMetaData, checkResponse.ruleMetadata, checkResponse.cached);
            }
        });
```

In case you used a URL in setConfig, you can also use the getLatest flag to get the latest config before making the callback
```java
Toggle.with(context).check("custom_network_component").getLatest().defaultState(Toggle.ENABLED).start(new cc.soham.toggle.callbacks.Callback() {
            @Override
            public void onStatusChecked(CheckResponse checkResponse) {
                updateUiAfterResponse(checkResponse.featureName, checkResponse.state, checkResponse.featureMetaData, checkResponse.ruleMetadata, checkResponse.cached);
            }
        });
```

The state of the feature can be found in checkResponse.state along with other things present in the config like metadata

###Other resources
You can find out more about Toggle specific configuration questions in our wiki You can find more information on the configuration parameters and how they work in the wiki [here](https://github.com/s0h4m/toggle/wiki).  
        
###Download

Get this via Gradle:
```groovy
compile 'cc.soham:toggle:0.1'
```
or Maven:
```xml
<dependency>
  <groupId>cc.soham</groupId>
  <artifactId>toggle</artifactId>
  <version>0.1</version>
</dependency>
```


###ProGuard

If you are using ProGuard you might need to add the following option:
```
-dontwarn cc.soham.toggle.**
```


###License

    Copyright 2016 Soham Mondal

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.        

###What's next
These are the things that are coming up in Toggle
- custom rule-matchers/checks (beyond the usual api-level/app-version/date etc.) that you can define
- faster lookups in check (use hashmaps to look up the states)
- more tests
- multiple configs