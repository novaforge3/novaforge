- Download the SYMPA sources of the 6.1.14 version on http://www.sympa.org/distribution and extract them.
- Be sure that the folder extracted is sympa-6.1.14
- Execute the patch command:

patch -p0 < sympa-6.1.14.patch

patch --binary -p0 < sympa-6.1.14.patch (under Windows)

- Copy 'etc' files to sympa etc directory set up
  - topics.conf
  - trusted_applications.conf
  - novaforge.conf
  - auth.conf

start date  [sympa-6.1.14.patch]
- Original patch 

30/01/2013 [sympa-6.1.14-002.patch]
- Improve function delete user to report when we can't delete list owner 

12/02/2013 [sympa-6.1.14-003.patch]
- Implement Single Sign Out for CAS logout request
- Correct the way to retrieve base_url using HTTP_X_FORWARDED_*

15/05/2013 [sympa-6.1.14-004.patch]
- Update return on soap api function create list, add subscriber and remove subscriber. 

07/08/2013 [sympa-6.1.14-008.patch]
- Change soap env argument ser:arg0 to arg0 to avoid incompatibility with Karaf CXF 

