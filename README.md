# Generate MVVM Template

#### Install Truss

```bash
curl -o /usr/local/bin/truss https://gitlab.silkrode.com.tw/golang/gen/raw/master/bin/truss
chmod 777 /usr/local/bin/truss
```

#### Build Project
```bash
sh build_project.sh com/dabenxiang/mvvm com.dabenxiang.mvvm
```

#### Build API
```bash
sh build_api.sh https://gitlab.silkrode.com.tw/team_mobile/genapiproto.git genapiproto com/dabenxiang/mvvm com.dabenxiang.mvvm
```
