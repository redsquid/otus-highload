#!/usr/bin/env tarantool
box.cfg {
   listen = 3301
}
box.once("bootstrap", function()

    box.schema.space.create('credential')
    credential_format = {
        {name = 'id', type = 'uuid'},
        {name = 'username', type = 'string'},
        {name = 'password', type = 'string'},
        {name = 'account_id', type = 'uuid'},
    }
    box.space.credential:format(credential_format)
    box.space.credential:create_index('credential_id_idx',{ type = 'TREE', unique=true, parts = {1, 'uuid'}})
    box.space.credential:create_index('credential_username_idx',{ type = 'TREE', unique=true, parts = {2, 'string'}})

    box.schema.space.create('token', {
        foreign_key = {
            space = 'credential',
            field = {credential_id = 'id'}
       }
    })
    token_format = {
       {name = 'id', type = 'uuid'},
       {name = 'credential_id', type = 'uuid'},
       {name = 'access_token', type = 'uuid'},
       {name = 'expiry_date', type = 'unsigned'},
    }
    box.space.token:format(token_format)
    box.space.token:create_index('access_token_idx',{ type = 'TREE', unique=true, parts = {3, 'uuid'}})

    uuid = require('uuid')
    dofile("./procedures.lua")
end)