docker run --name mytarantool -p3301:3301 -d tarantool/tarantool

docker exec -t -i mytarantool console



docker run -ti -p 3301:3301 -e TARANTOOL_USER_NAME=highload -e TARANTOOL_USER_PASSWORD=highload -v /c/work/otus/highload/otus-highload/highload-auth/src/main/resources/lua:/opt/tarantool/ tarantool/tarantool tarantool /opt/tarantool/init.lua

box.space.credential.index.credential_username_idx:select({username})

createToken('username0', 'qwerty', 1806435034);