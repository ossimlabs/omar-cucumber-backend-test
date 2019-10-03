@o2_basemap
Feature: O2Basemap

  Scenario Outline: [O2BM-01] A call is made to a basemap tile
    When a call is made to a basemap <image>
    Then a <image> is returned
    Examples:
      | image |
      | 1     |
      | 2     |
      | 3     |
      | 4     |
      | 5     |
      | 6     |
      | 7     |
      | 8     |
      | 9     |
      | 10    |
      | 11    |
      | 12    |
      | 13    |
      | 14    |
      | 15    |
      | 16    |

  Scenario Outline: [O2BM-02] Multiple calls are made to a basemap tile
    When multiple calls are made to a basemap <image>
    Then an <image> is returned all times
    Examples:
      | image |
      | 1     |
      | 2     |
      | 3     |
      | 4     |
      | 5     |
      | 6     |
      | 7     |
      | 8     |
      | 9     |
      | 10    |
      | 11    |
      | 12    |
      | 13    |
      | 14    |
      | 15    |
      | 16    |