import request from '@/utils/request'

// 查询数据清洗规则配置列表
export function listDataCleanConfig(query) {
  return request({
    url: '/dataClean/dataCleanConfig/list',
    method: 'get',
    params: query
  })
}

// 查询数据清洗规则配置详细
export function getDataCleanConfig(id) {
  return request({
    url: '/dataClean/dataCleanConfig/' + id,
    method: 'get'
  })
}

// 新增数据清洗规则配置
export function addDataCleanConfig(data) {
  return request({
    url: '/dataClean/dataCleanConfig',
    method: 'post',
    data: data
  })
}

// 修改数据清洗规则配置
export function updateDataCleanConfig(data) {
  return request({
    url: '/dataClean/dataCleanConfig',
    method: 'put',
    data: data
  })
}

// 删除数据清洗规则配置
export function delDataCleanConfig(id) {
  return request({
    url: '/dataClean/dataCleanConfig/' + id,
    method: 'delete'
  })
}
