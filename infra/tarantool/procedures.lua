function createCredential(id, username, password, account_id)
  local c = box.space.credential.index.credential_username_idx:select({username})
  if next(c) ~= nil then
    return false
  end
  box.space.credential:insert({id, username, password, account_id})
  return true
end

function createToken(username, password, expiry_date)
  local c = box.space.credential.index.credential_username_idx:select({username})
  if next(c) == nil or c[1][3] ~= password then
    return nil
  end
  local token = uuid.new()
  box.space.token:insert({uuid.new(), c[1][1], token, expiry_date})
  return c[1][4]:str() .. ':' .. token:str()
end

function validateToken(token, date)
  local t = box.space.token.index.access_token_idx:select({token})
  if next(t) == nil or date > t[1][4] then
    return nil
  end
  local c = box.space.credential.index.credential_id_idx:select({t[1][2]})
  if next(c) == nil then
    return nil
  end
  return c[1][4]
end