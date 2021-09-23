
Feature: The Operations of Cluster

Background:
  * url   zeppelin.base_url
  * path  zeppelin.api_version + zeppelin.path.cluster

  * def data = read('classpath:data/warehouse/create-cluster.json')


Scenario: Test Cluster Ops
  - 1. Create a cluster, and store its ID
  - 2. Get Cluster' info By ID, and verify that it meets expectations
  - 3. Delete cluster By ID and clean up.

  * copy create_arg = data.req
  * set create_arg.name = 'cluster-testcases-1'
  * set create_arg.creator = 'cluster-tester-1'

  # create
  * def query_arg = call read('classpath:testcases/unit/warehouse/cluster-testcases.feature@create-cluster') create_arg

  # query and verify
  * def actual = call read('classpath:testcases/unit/warehouse/cluster-testcases.feature@query-cluster') query_arg
  * match actual.response == data.excepted

  # delete
  * def delete_arg = query_arg
  * call read('classpath:testcases/unit/warehouse/cluster-testcases.feature@delete-cluster') delete_arg


Scenario: Toggle Cluster On/Off
  - 1. Create a cluster and store its ID (status should be 'RUNNING')
  - 2. Toggle cluster off, and get cluster info
  - 3. Verify cluster's status

  * copy create_arg = data.req
  * set create_arg.name = 'cluster-testcases-2'
  * set create_arg.creator = 'cluster-tester-2'

  * def query_arg = call read('classpath:testcases/unit/warehouse/cluster-testcases.feature@create-cluster') create_arg
  * def resp = call read('classpath:testcases/unit/warehouse/cluster-testcases.feature@query-cluster') query_arg
  * match resp.body.status == 'RUNNING'

  # Stop Cluster
  * def toggle_arg = {}
  * set toggle_arg.sparkClusterId = resp.body.sparkClusterId
  * set toggle_arg.operation = 'STOP'
  * call read('classpath:testcases/unit/warehouse/cluster-testcases.feature@toggle-cluster') toggle_arg

  * def resp2 = call read('classpath:testcases/unit/warehouse/cluster-testcases.feature@query-cluster') create_arg
  * match resp2.body.status == 'STOP'


Scenario: Attach Notebook to Cluster
  - 1. Create a cluster and store its ID (attachNotebooks should be null -> [])
  - 2. Create a notebook and get its ID
  - 3. Attach notebook to cluster, then verify cluster info whether it's correct


##### JUST OP, Do not verify ####
@ignore
@create-cluster
Scenario: Create Cluster,
  - ARG: create_arg

  Given request create_arg
  When  method post
  Then  status 200
  And   match response == {status: 'OK', message: '', body: '#ignore'}
  And   def sparkClusterId = response.body.sparkClusterId

@ignore
@query-cluster
Scenario: Get Cluster's Info By ID
  - ARG: query_arg

  Given path zeppelin.api_version + zeppelin.path.cluster
  And   path query_arg.sparkClusterId
  When  method get
  Then  status 200
  And   match response == {status: 'OK', message: '', body: '#ignore'}
  And   def actual = response.body

@ignore
@delete-cluster
Scenario: Delete Cluster By ID
  - ARG: delete_arg

  Given request delete_arg.sparkClusterId
  When  method delete
  Then  status 200
  And   match response == {status: 'OK', message: ''}

@ignore
@toggle-cluster
Scenario: Toggle cluster to other status
  - ARG: toggle_arg

  Given path zeppelin.api_version + zeppelin.path.cluster
  And   path toggle_arg.sparkClusterId
  And   request toggle_arg

  When  method patch
  Then  match response == {status: 'OK', message: ''}