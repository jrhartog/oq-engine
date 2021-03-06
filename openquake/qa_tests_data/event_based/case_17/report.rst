Event Based Hazard QA Test, Case 17
===================================

============== ===================
checksum32     1_693_427_193      
date           2020-03-13T11:21:25
engine_version 3.9.0-gitfb3ef3a732
============== ===================

num_sites = 1, num_levels = 3, num_rlzs = 5

Parameters
----------
=============================== ==================
calculation_mode                'preclassical'    
number_of_logic_tree_samples    5                 
maximum_distance                {'default': 200.0}
investigation_time              1.0               
ses_per_logic_tree_path         3                 
truncation_level                2.0               
rupture_mesh_spacing            2.0               
complex_fault_mesh_spacing      2.0               
width_of_mfd_bin                1.0               
area_source_discretization      20.0              
pointsource_distance            {'default': {}}   
ground_motion_correlation_model None              
minimum_intensity               {}                
random_seed                     106               
master_seed                     0                 
ses_seed                        106               
=============================== ==================

Input files
-----------
======================= ============================================================
Name                    File                                                        
======================= ============================================================
gsim_logic_tree         `gsim_logic_tree.xml <gsim_logic_tree.xml>`_                
job_ini                 `job.ini <job.ini>`_                                        
source_model_logic_tree `source_model_logic_tree.xml <source_model_logic_tree.xml>`_
======================= ============================================================

Composite source model
----------------------
========= ======= ================
smlt_path weight  num_realizations
========= ======= ================
b1        0.60000 3               
b2        0.40000 2               
========= ======= ================

Required parameters per tectonic region type
--------------------------------------------
====== ================== ========= ========== ==========
grp_id gsims              distances siteparams ruptparams
====== ================== ========= ========== ==========
0      '[SadighEtAl1997]' rrup      vs30       mag rake  
1      '[SadighEtAl1997]' rrup      vs30       mag rake  
====== ================== ========= ========== ==========

Number of ruptures per source group
-----------------------------------
====== ========= ============ ============
grp_id num_sites num_ruptures eff_ruptures
====== ========= ============ ============
0      0.02564   39           39          
1      0.14286   7            7.00000     
====== ========= ============ ============

Slowest sources
---------------
========= ====== ==== ============ ========= ========= ============
source_id grp_id code num_ruptures calc_time num_sites eff_ruptures
========= ====== ==== ============ ========= ========= ============
1         0      P    39           0.00166   0.02564   39          
2         1      P    7            0.00162   0.14286   7.00000     
========= ====== ==== ============ ========= ========= ============

Computation times by source typology
------------------------------------
==== =========
code calc_time
==== =========
P    0.00328  
==== =========

Information about the tasks
---------------------------
================== ======= ========= ======= ======= =======
operation-duration mean    stddev    min     max     outputs
preclassical       0.00230 2.242E-05 0.00228 0.00231 2      
read_source_model  0.00240 9.566E-04 0.00172 0.00307 2      
================== ======= ========= ======= ======= =======

Data transfer
-------------
================= =========================================== ========
task              sent                                        received
read_source_model converter=664 B fname=202 B srcfilter=8 B   3.22 KB 
preclassical      srcs=2.51 KB params=1.25 KB srcfilter=446 B 738 B   
================= =========================================== ========

Slowest operations
------------------
=========================== ========= ========= ======
calc_66940                  time_sec  memory_mb counts
=========================== ========= ========= ======
composite source model      0.06015   0.0       1     
total read_source_model     0.00479   0.0       2     
total preclassical          0.00459   1.53125   2     
store source_info           0.00209   0.0       1     
aggregate curves            7.679E-04 0.0       2     
splitting/filtering sources 3.755E-04 0.0       2     
=========================== ========= ========= ======